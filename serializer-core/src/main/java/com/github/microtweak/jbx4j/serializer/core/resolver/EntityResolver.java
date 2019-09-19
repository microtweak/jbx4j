package com.github.microtweak.jbx4j.serializer.core.resolver;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * Provides the abstraction of a component capable of solving a JSON (or any other format) in a JPA Entity.
 * The JPA entity resolution means looking for it in the Persistence Context or simply instantiate it manually with customized parameters.
 * <br>
 * <br>
 * During the deserialization process, the most appropriate EntityResolver for each situation is selected because a JPA entity may have one or more EntityResolver.
 * <br>
 * <br>
 * The JSON Binding frameworks (such as Jackson, GSON, etc.) simply instantiate the JPA Entity by copying the JSON values into it.
 * These frameworks do not usually worry about relationship properties that may not be loaded, causing the JPA provider to think that some information should be erased from the database.
 *
 * @param <E> Type of entity to be resolved.
 *
 * @author Marcos Koch Salvador
 * @since 1.0.0
 */
public interface EntityResolver<E> {

    int DEFAULT_ORDINAL = 100;

    /**
     * Returns the ordinal value of this EntityResolver. If an entity has multiple implementations of EntityResolver, the implementation with the highest ordinal value is preferred.
     *
     * @return Ordinal Value of this EntityResolver
     */
    default int getOrdinal() {
        return DEFAULT_ORDINAL;
    }

    /**
     * Checks whether the EntityResolver instance is able to resolve the Entity.
     *
     * @param parent If you are resolving an entity that is a nested attribute/property, this parameter represents the object that contains this attribute/property.
     *               If the entity represents the root element of JSON (or any other format) this parameter will be null.
     * @param rawType Class representing the JPA Entity.
     * @param data Component that transports JSON information (or any other format).
     * @param annotations If you are resolving an entity that is a nested attribute / property, this parameter will represent a List with all annotations of this attribute / property. Otherwise, it will be an empty list.
     * @return true if the EntityResolver instance is able to resolve the entity. Otherwise false.
     */
    boolean canResolve(Object parent, Class<E> rawType, JpaEntityData<E> data, List<Annotation> annotations);

    /**
     * Performs the resolution logic of JPA Entity.
     *
     * @param parent If you are resolving an entity that is a nested attribute/property, this parameter represents the object that contains this attribute/property.
     *               If the entity represents the root element of JSON (or any other format) this parameter will be null.
     * @param rawType Class representing the JPA Entity.
     * @param data Component that transports JSON information (or any other format).
     * @param annotations If you are resolving an entity that is a nested attribute / property, this parameter will represent a List with all annotations of this attribute / property. Otherwise, it will be an empty list.
     * @return Instance of resolved Entity.
     */
    E resolve(Object parent, Class<E> rawType, JpaEntityData<E> data, List<Annotation> annotations);

}
