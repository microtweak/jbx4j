package com.github.microtweak.jbx4j.descriptor.spi;

import com.github.microtweak.jbx4j.descriptor.exception.UnsupportedJpaProviderException;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Utilities for reflection in JPA classes
 *
 * @author Marcos Koch Salvador
 * @since 1.0.0
 */
public abstract class ReflectionHelper {

    private static ReflectionHelper INSTANCE;

    public static synchronized ReflectionHelper getInstance() {
        if (INSTANCE == null) {
            for (ReflectionHelper impl : ServiceLoader.load(ReflectionHelper.class)) {
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
     * Checks whether the instance of an object is a JPA Entity.
     *
     * @param bean Instance of object to be verified.
     * @return true if it is a JPA Entity or false otherwise.
     */
    public boolean isEntity(Object bean) {
        return (bean != null && isEntity(bean.getClass()));
    }

    /**
     * Check if the class provided is a JPA Entity.
     *
     * @param clazz Class to be verified.
     * @return true if it is a JPA Entity or false otherwise.
     */
    public boolean isEntity(Class<?> clazz) {
        return (clazz != null && clazz.isAnnotationPresent(Entity.class));
    }

    /**
     * Checks whether the instance of an object is a JPA Embeddable.
     *
     * @param bean Instance of object to be verified.
     * @return true if it is a JPA Embeddable or false otherwise.
     */
    public boolean isEmbeddable(Object bean) {
        return (bean != null && isEmbeddable(bean.getClass()));
    }

    /**
     * Check if the class provided is a JPA Embeddable.
     *
     * @param clazz Class to be verified.
     * @return true if it is a JPA Embeddable or false otherwise.
     */
    public boolean isEmbeddable(Class<?> clazz) {
        return (clazz != null && clazz.isAnnotationPresent(Embeddable.class));
    }

    /**
     * Walk across class hierarchy looking all declared attributes.
     *
     * @param clazz Base class to start attribute search.
     * @return A Set of Field with all attributes found.s
     */
    public final Set<Field> findAllEntityAttributes(Class<?> clazz) {
        Set<Field> allAttributes = new LinkedHashSet<>();

        if (!Object.class.equals(clazz.getSuperclass())) {
            allAttributes.addAll( findAllEntityAttributes(clazz.getSuperclass()) );
        }

        for (Field attr : clazz.getDeclaredFields()) {
            if (!isEntityProperty(attr)) {
                continue;
            }

            int mod = attr.getModifiers();

            if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                continue;
            }

            allAttributes.add(attr);
        }

        return allAttributes;
    }


    /**
     * Gets the actual class behind the proxy created by the JPA provider.
     *
     * @param proxy Proxy object class
     * @return Class behind the proxy
     */
    public Class<?> getJpaClass(Class<?> proxy) {
        return proxy;
    }

    protected boolean isEntityProperty(Field field) {
        return true;
    }

}