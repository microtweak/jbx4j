package com.github.microtweak.jbx4j.serializer.jackson;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.microtweak.jbx4j.descriptor.core.JpaDescriptor;
import com.github.microtweak.jbx4j.descriptor.core.attribute.*;
import com.github.microtweak.jbx4j.descriptor.core.spi.ReflectionHelper;
import com.github.microtweak.jbx4j.serializer.core.exception.JpaEntityBindingException;
import com.github.microtweak.jbx4j.serializer.core.resolver.EntityResolver;
import com.github.microtweak.jbx4j.serializer.core.resolver.JpaEntityData;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Id;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author Marcos Koch Salvador
 * @since 1.0.0
 */
public class EntityDeserializer extends JsonDeserializer<Object> {

    private Jbx4jJacksonModule module;
    private ObjectMapper mapper;
    private Class<?> rootType;

    public EntityDeserializer(Jbx4jJacksonModule module, ObjectMapper mapper, Class<?> rootType) {
        this.module = module;
        this.mapper = mapper;
        this.rootType = rootType;
    }

    @Override
    public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode rootNode = jp.getCodec().readTree(jp);

        if (rootNode.isNull()) {
            return null;
        }

        if (rootNode.isArray()) {

            Collection<Object> elements = getCollectionImpl(rootType);

            for (JsonNode itemNode : rootNode) {
                elements.add(deserializeJpaGraph(null, rootType, itemNode, null, true));
            }

            return elements;

        }

        return deserializeJpaGraph(null, rootType, rootNode, null, true);
    }

    private Object deserializeJpaGraph(Object declaredAt, Class<?> entityType, JsonNode jsonNode,
                                       List<Annotation> annotations, boolean updateEntityAttributes) throws IOException {

        if (annotations == null) {
            annotations = Collections.emptyList();
        }

        JpaDescriptor descriptor = JpaDescriptor.of(entityType);

        Object entity = null;

        if (descriptor.isEntity()) {
            entity = resolveJpaEntity(declaredAt, entityType, jsonNode, annotations);

            if (!updateEntityAttributes) {
                return entity;
            }
        }

        if (entity == null) {
            entity = newInstance(entityType);
        }

        for (EntityAttribute attribute : descriptor.getAttributes()) {
            JsonNode attrNode = jsonNode.get(attribute.getName());

            if (attribute.isAnyAnnotationPresent(Id.class, EmbeddedId.class, JsonIgnore.class)
                    || !attribute.isGetterAndSetterPresent() || Objects.isNull(attrNode)) {

                continue;
            }

            Object value = null;

            if (attribute instanceof CommonEntityAttribute) {
                value = mapper.treeToValue(attrNode, attribute.getType());
            }

            else if (attribute instanceof RelationshipEntityAttribute) {
                value = deserializeJpaRelationship(entity, (RelationshipEntityAttribute<?>) attribute, attrNode);
            }

            attribute.set(entity, value);
        }

        return entity;
    }

    private Object deserializeJpaRelationship(Object declaredAt, RelationshipEntityAttribute<?> attr, JsonNode relationshipNode) throws IOException {
        if (relationshipNode.isNull()) {
            return null;
        }

        boolean updateEntityAttributes = attr.hasAnyCascade(CascadeType.ALL, CascadeType.PERSIST, CascadeType.MERGE);

        if (attr instanceof OneToOneRelationshipEntityAttribute || attr instanceof ManyToOneRelationshipEntityAttribute) {
            return deserializeJpaGraph(declaredAt, attr.getType(), relationshipNode, attr.getAnnotations(), updateEntityAttributes);
        }

        if (attr instanceof ManyToManyRelationshipEntityAttribute || attr instanceof OneToManyRelationshipEntityAttribute
                || attr instanceof ElementCollectionRelationshipEntityAttribute) {

            if (Collection.class.isAssignableFrom(attr.getType())) {
                return deserializeJsonArrayAsJpaCollection(declaredAt, attr, relationshipNode, updateEntityAttributes);
            }

            if (Map.class.isAssignableFrom(attr.getType())) {
                return deserializeJsonEntryArrayAsJpaMap(declaredAt, attr, relationshipNode, updateEntityAttributes);
            }
        }

        return null;
    }

    private Collection deserializeJsonArrayAsJpaCollection(Object declaredAt, RelationshipEntityAttribute<?> attr, JsonNode relationshipNode, boolean updateEntityAttributes) throws IOException {
        Collection<Object> elements = getCollectionImpl(attr.getType());
        Class<?> itemType = attr.getTypeParameters().get(0);

        for (JsonNode itemNode : relationshipNode) {
            elements.add(deserializeJsonObjectAsJavaObject(declaredAt, itemType, itemNode, attr.getAnnotations(), updateEntityAttributes));
        }

        return elements;
    }

    private Map deserializeJsonEntryArrayAsJpaMap(Object declaredAt, RelationshipEntityAttribute<?> attr, JsonNode relationshipNode, boolean updateEntityAttributes) throws IOException {
        Map elements = new LinkedHashMap<>();

        Class<?> keyType = attr.getTypeParameters().get(0);
        Class<?> valueType = attr.getTypeParameters().get(1);

        for (JsonNode entryNode : relationshipNode) {
            Object key = deserializeJsonObjectAsJavaObject(declaredAt, keyType, entryNode.get("key"), attr.getAnnotations(), updateEntityAttributes);

            Object value = deserializeJsonObjectAsJavaObject(declaredAt, valueType, entryNode.get("value"), attr.getAnnotations(), updateEntityAttributes);

            elements.put(key, value);
        }

        return elements;
    }

    private Object deserializeJsonObjectAsJavaObject(Object declaredAt, Class<?> type, JsonNode jsonNode, List<Annotation> annotations, boolean updateEntityAttributes) throws IOException {
        if (!ReflectionHelper.getInstance().isEntity(type) && !ReflectionHelper.getInstance().isEmbeddable(type)) {
            return mapper.treeToValue(jsonNode, type);
        }

        return deserializeJpaGraph(declaredAt, type, jsonNode, annotations, updateEntityAttributes);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private <E> Object resolveJpaEntity(Object parent, Class<E> rawType, JsonNode jsonNode, List<Annotation> annotations) throws JsonProcessingException {
        JpaEntityData<E> data = new JacksonJpaEntityData<>(rawType, mapper, jsonNode);

        EntityResolver resolver = module.getResolverManager().lookupResolver(parent, rawType, data, annotations);
        return resolver.resolve(parent, rawType, data, annotations);
    }

    private Object newInstance(Class<?> rawType) {
        try {
            Constructor<?> noArgsConstructor = rawType.getDeclaredConstructor();
            noArgsConstructor.setAccessible(true);

            return noArgsConstructor.newInstance();
        } catch (NoSuchMethodException | SecurityException e) {

            throw new JpaEntityBindingException("Could not find the no-args constructor in entity " + rawType.getName(), e);

        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {

            throw new JpaEntityBindingException("Failed to call the no-args constructor in entity " + rawType.getName(), e);

        }
    }

    private Collection<Object> getCollectionImpl(Class<?> interfaceRawType) {
        if (Set.class.isAssignableFrom(interfaceRawType)) {
            return new LinkedHashSet<>();
        }

        return new ArrayList<>();
    }

}
