package com.github.microtweak.jbx4j.descriptor.core;

import com.github.microtweak.jbx4j.descriptor.core.attribute.*;
import com.github.microtweak.jbx4j.descriptor.core.spi.ReflectionHelper;

import javax.persistence.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Object capable of describing a JPA Entity or Embeddable with its attributes.
 *
 * @author Marcos Koch Salvador
 * @since 1.0.0
 */
public class JpaDescriptor {

    private static final Map<Class<?>, JpaDescriptor> CACHE = new HashMap<>();

    private Class<?> jpaClass;

    private boolean isEntity;

    private boolean isEmbeddable;

    private String entityName;

    private Map<String, EntityAttribute> attributes;

    private JpaDescriptor(Class<?> clazz, boolean isEntity, boolean isEmbeddable) {
        this.jpaClass = clazz;

        this.isEntity = isEntity;

        this.isEmbeddable = isEmbeddable;

        if (isEntity) {
            entityName = Optional.ofNullable(clazz.getAnnotation(Entity.class))
                .map(ann -> ann.name())
                .filter(name -> !name.isEmpty())
                .orElse(clazz.getSimpleName());
        }

        Map<String, EntityAttribute> attributes = ReflectionHelper.getInstance().findAllEntityAttributes(clazz).stream()
            .map(JpaPropertyInfo::new)
            .map(this::toEntityAttribute)
            .collect(Collectors.toMap(EntityAttribute::getName, Function.identity(), (e1, e2) -> e1, LinkedHashMap::new));

        this.attributes = Collections.unmodifiableMap(attributes);
    }

    /**
     * Locate a JpaDescriptor corresponding to the requested class.
     *
     * @param clazz Class used to find JpaDescriptor
     * @return An instance of JpaDescriptor if the given class is an JPA Entity or Embaddable. Otherwise, returns null.
     */
    public synchronized static JpaDescriptor of(Class<?> clazz) {
        boolean jpaEntity = ReflectionHelper.getInstance().isEntity(clazz);
        boolean jpaEmbeddable = ReflectionHelper.getInstance().isEmbeddable(clazz);

        if (!jpaEntity && !jpaEmbeddable) {
            return null;
        }

        clazz = ReflectionHelper.getInstance().getJpaClass(clazz);
        JpaDescriptor metadata = CACHE.get(clazz);

        if (metadata == null) {
            metadata = new JpaDescriptor(clazz, jpaEntity, jpaEmbeddable);
            CACHE.put(clazz, metadata);
        }

        return metadata;
    }

    /**
     * Locate a JpaDescriptor corresponding to the object instance.
     *
     * @param entity Class used to find JpaDescriptor
     * @return An instance of JpaDescriptor if the given object instance is an JPA Entity or Embaddable. Otherwise, returns null.
     * @throws NullPointerException If the object instance is null
     */
    public synchronized static JpaDescriptor of(Object entity) {
        Objects.requireNonNull(entity, "Can not get descriptor from a null reference");
        return of(entity.getClass());
    }

    private EntityAttribute toEntityAttribute(JpaPropertyInfo info) {
        ManyToMany manyToMany = info.getAnnotation(ManyToMany.class);
        if (manyToMany != null) {
            return new ManyToManyRelationshipEntityAttribute(info, manyToMany);
        }

        ManyToOne manyToOne = info.getAnnotation(ManyToOne.class);
        if (manyToOne != null) {
            return new ManyToOneRelationshipEntityAttribute(info, manyToOne);
        }

        OneToMany oneToMany = info.getAnnotation(OneToMany.class);
        if (oneToMany != null) {
            return new OneToManyRelationshipEntityAttribute(info, oneToMany);
        }

        OneToOne oneToOne = info.getAnnotation(OneToOne.class);
        if (oneToOne != null) {
            return new OneToOneRelationshipEntityAttribute(info, oneToOne);
        }

        ElementCollection elementCollection = info.getAnnotation(ElementCollection.class);
        if (elementCollection != null) {
            return new ElementCollectionRelationshipEntityAttribute(info, elementCollection);
        }

        return new CommonEntityAttribute(info);
    }

    public Class<?> getJpaClass() {
        return jpaClass;
    }

    public boolean isEntity() {
        return isEntity;
    }

    public boolean isEmbeddable() {
        return isEmbeddable;
    }

    /**
     * Returns the name of the JPA entity.
     *
     * Consider the name of the class as the name of the Entity.
     * If the name is overwritten by the @Entity annotation, the value given in the "name" parameter of this annotation will be considered.
     *
     * @return Returns the entity name according to rule explained above or null if JpaDescriptor is referring to a JPA Embeddable.
     */
    public String getEntityName() {
        return entityName;
    }

    /**
     * All EntityAttribute's in JpaDescriptor.
     *
     * @return A Collection with all EntityAttribute's.
     */
    public Collection<EntityAttribute> getAttributes() {
        return attributes.values();
    }

    /**
     * Only EntityAttribute that contain the declared Getter method.
     *
     * @return A Collection with EntityAttribute only containing the declared Getter method
     */
    public Collection<EntityAttribute> getReadableAttributes() {
        Collection<EntityAttribute> onlyReadable = attributes.values().stream()
            .filter(attr -> attr.isGetterPresent())
            .collect(Collectors.toCollection(LinkedHashSet::new));

        return Collections.unmodifiableCollection(onlyReadable);
    }

    /**
     * Gets an EntityAttribute by the name of the attribute/property in the Entity class.
     *
     * @param attributeName Name of the attribute to be searched for
     * @return Returns the found EntityAttribute or null if not found
     */
    public EntityAttribute getAttribute(String attributeName) {
        return attributes.get(attributeName);
    }

}
