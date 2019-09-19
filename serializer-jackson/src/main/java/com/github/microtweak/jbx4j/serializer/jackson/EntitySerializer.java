package com.github.microtweak.jbx4j.serializer.jackson;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.github.microtweak.jbx4j.descriptor.core.JpaDescriptor;
import com.github.microtweak.jbx4j.descriptor.core.attribute.*;
import com.github.microtweak.jbx4j.descriptor.core.spi.ReflectionHelper;
import com.github.microtweak.jbx4j.serializer.core.CircularReferenceDetector;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * @author Marcos Koch Salvador
 * @since 1.0.0
 */
public class EntitySerializer extends JsonSerializer<Object> {

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        CircularReferenceDetector circularDetector = new CircularReferenceDetector();
        serializeJpaGraph(gen, value, circularDetector);
    }

    private void serializeJpaGraph(JsonGenerator jg, Object entity, CircularReferenceDetector circularDetector) throws IOException {
        if (entity == null) {
            jg.writeNull();
            return;
        }

        JpaDescriptor descriptor = JpaDescriptor.of(entity);

        circularDetector = circularDetector.add(descriptor.getJpaClass());

        jg.writeStartObject();

        for (EntityAttribute attr : descriptor.getAttributes()) {
            if (!attr.isGetterPresent() || attr.isAnnotationPresent(JsonIgnore.class) || !attr.isLoaded(entity)) {
                continue;
            }

            Object value = attr.get(entity);

            if (attr instanceof CommonEntityAttribute) {
                jg.writeObjectField(attr.getName(), value);
            }

            else if (attr instanceof RelationshipEntityAttribute) {
                serializeJpaRelationship(jg, value, (RelationshipEntityAttribute<?>) attr, circularDetector);
            }
        }

        jg.writeEndObject();
    }

    private void serializeJpaRelationship(JsonGenerator jg, Object value, RelationshipEntityAttribute<?> attr, CircularReferenceDetector circularDetector) throws IOException {
        if (attr instanceof OneToOneRelationshipEntityAttribute || attr instanceof ManyToOneRelationshipEntityAttribute) {

            if (circularDetector.contains(attr.getType())) {
                return;
            }

            jg.writeFieldName(attr.getName());

            serializeJpaGraph(jg, value, circularDetector);
        }

        else if (attr instanceof ManyToManyRelationshipEntityAttribute || attr instanceof OneToManyRelationshipEntityAttribute
                || attr instanceof ElementCollectionRelationshipEntityAttribute) {

            if (value == null) {
                jg.writeNullField(attr.getName());
                return;
            }

            if (circularDetector.contains(attr.getTypeParameters())) {
                return;
            }

            jg.writeFieldName(attr.getName());

            if (value instanceof Collection) {
                serializeJpaCollectionAsArray(jg, (Collection<?>) value, circularDetector);
            }

            else if (value instanceof Map) {
                serializeJpaMapAsJsonEntryArray(jg, (Map<?, ?>) value, circularDetector);
            }
        }
    }

    private void serializeJpaCollectionAsArray(JsonGenerator jg, Collection<?> elements, CircularReferenceDetector circularDetector) throws IOException {
        jg.writeStartArray();

        for (Object element : elements) {
            serializeJavaObjectAsJsonObject(jg, element, circularDetector);
        }

        jg.writeEndArray();
    }

    private void serializeJpaMapAsJsonEntryArray(JsonGenerator jg, Map<?, ?> elements, CircularReferenceDetector circularDetector) throws IOException {
        jg.writeStartArray();

        for (Map.Entry<?, ?> entry : elements.entrySet()) {
            jg.writeStartObject();

            jg.writeFieldName("key");
            serializeJavaObjectAsJsonObject(jg, entry.getKey(), circularDetector);

            jg.writeFieldName("value");
            serializeJavaObjectAsJsonObject(jg, entry.getValue(), circularDetector);

            jg.writeEndObject();
        }

        jg.writeEndArray();
    }

    private void serializeJavaObjectAsJsonObject(JsonGenerator jg, Object value, CircularReferenceDetector circularDetector) throws IOException {
        if (!ReflectionHelper.getInstance().isEntity(value) && !ReflectionHelper.getInstance().isEmbeddable(value)) {
            jg.writeObject(value);
            return;
        }

        serializeJpaGraph(jg, value, circularDetector);
    }
}