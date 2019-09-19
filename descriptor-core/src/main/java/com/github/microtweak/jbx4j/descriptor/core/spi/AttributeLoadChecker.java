package com.github.microtweak.jbx4j.descriptor.core.spi;

import com.github.microtweak.jbx4j.descriptor.core.exception.UnsupportedJpaProviderException;

import java.util.ServiceLoader;

/**
 * Strategy to check if an attribute is lazy or has been loaded by the JPA provider.
 *
 * @author Marcos Koch Salvador
 * @since 1.0.0
 */
public abstract class AttributeLoadChecker {

    private static AttributeLoadChecker INSTANCE;

    public static synchronized AttributeLoadChecker getInstance() {
        if (INSTANCE == null) {
            for (AttributeLoadChecker impl : ServiceLoader.load(AttributeLoadChecker.class)) {
                INSTANCE = impl;
                break;
            }

            if (INSTANCE == null) {
                final String msg = String.format("No implementation of %s for your JPA provider!", AttributeLoadChecker.class.getSimpleName());
                throw new UnsupportedJpaProviderException(msg);
            }
        }

        return INSTANCE;
    }

    /**
     * Check if an attribute of an entity class is lazy or has been loaded
     *
     * @param entity Instance of entity class to be verified
     * @param attrName Name of attribute to be checked
     * @return "true" if the attribute is loaded and "false" if it is in the lazy state
     */
    public abstract boolean isLoaded(Object entity, String attrName);

}
