package com.github.microtweak.jbx4j.serializer.resolver;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Optional;

/**
 * Component that transports JSON information (or any other format) to EntityResolver to locate the JPA Entity.
 *
 * @author Marcos Koch Salvador
 * @since 1.0.0
 */
public interface JpaEntityData<E> {

    /**
     * Gets the value of an attribute/property
     *
     * @param attrName Name of the attribute/property to find the value.
     * @return Object containing the desired attribute value. May be null if attribute is not found
     */
    Object get(String attrName);

    /**
     * Gets the typed value of an attribute/property
     *
     * @param attrName Name of the attribute/property to find the value.
     * @param type Class representing the type of requested value. It is recommended to use wrarpper types and String.
     * @param <V> Generic type that represents the typing of the requested value
     * @return Value of the attribute according to the requested type. May be null if attribute is not found
     * @throws IllegalArgumentException If the value is not an instance of the requested type
     */
    default <V extends Serializable> V get(String attrName, Class<V> type) {
        Object value = get(attrName);

        if (value != null && !type.isInstance(value)) {
            String msg = MessageFormat.format("Unable to get property \"{0}\" as \"{1}\" of object to be deserialized", attrName, type.getSimpleName());
            throw new IllegalArgumentException(msg);
        }

        return (V) value;
    }

    /**
     * Gets java.util.Optional with the value of an attribute/property
     *
     * @param attrName Name of the attribute/property to find the value.
     * @return Optional containing the desired attribute value. May be empty if attribute is not found
     */
    default Optional<Object> getOptional(String attrName) {
        return Optional.ofNullable( get(attrName) );
    }

    /**
     * Gets java.util.Optional with the typed value of an attribute/property
     *
     * @param attrName Name of the attribute/property to find the value.
     * @param type Class representing the type of requested value. It is recommended to use wrarpper types and String.
     * @param <V> Generic type that represents the typing of the requested value
     * @return Optional with value of the attribute according to the requested type. May be empty if attribute is not found
     * @throws IllegalArgumentException If the value is not an instance of the requested type
     */
    default <V extends Serializable> Optional<V> getOptional(String attrName, Class<V> type) {
        return Optional.ofNullable( get(attrName, type) );
    }

}
