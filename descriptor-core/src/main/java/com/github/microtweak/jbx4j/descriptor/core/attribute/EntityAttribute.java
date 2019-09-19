package com.github.microtweak.jbx4j.descriptor.core.attribute;

import com.github.microtweak.jbx4j.descriptor.core.JpaPropertyInfo;
import com.github.microtweak.jbx4j.descriptor.core.exception.JpaPropertyAccessException;
import com.github.microtweak.jbx4j.descriptor.core.spi.AttributeLoadChecker;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Provides an abstraction of an attribute of a JPA Entity or Embeddable
 *
 * @author Marcos Koch Salvador
 * @since 1.0.0
 */
public class EntityAttribute {

    private static final Logger LOG = Logger.getLogger(EntityAttribute.class.getName());

    private JpaPropertyInfo info;

    private Class<?> type;
    private List<Class<?>> typeParameters;

    protected EntityAttribute(JpaPropertyInfo info) {
        this.info = info;

        Type genericType = info.getField().getGenericType();

        if (genericType instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) genericType;

            type = (Class<?>) pt.getRawType();

            typeParameters = Collections.unmodifiableList(
                Stream.of(pt.getActualTypeArguments()).map(t -> (Class<?>) t).collect(toList())
            );
        }

        else if (genericType instanceof Class) {
            type = (Class<?>) genericType;
            typeParameters = Collections.emptyList();
        }
    }

    /**
     * Class in which the attribute in question was declared.
     *
     * @return A java.lang.Class of the entity in which the attribute was declared.
     */
    public Class<?> getDeclaringClass() {
        return info.getField().getDeclaringClass();
    }

    /**
     * Name of the attribute declared in the Java class.
     *
     * @return A string containing the attribute name.
     */
    public String getName() {
        return info.getField().getName();
    }

    /**
     * Type of attribute declared in Java class.
     *
     * @return A java.lang.Class representing the attribute type.
     */
    public Class<?> getType() {
        return type;
    }

    /**
     * Gets a list of generic parameters of the declared attribute (List, Map, etc.). All genetic types of the attribute will be returned by this method.
     *
     * @return A list containing java.lang.Class attributes of each generic parameter.
     */
    public List<Class<?>> getTypeParameters() {
        return typeParameters;
    }

    /**
     * Check if the attribute was previously loaded by the JPA provider. You must provide the entity instance to perform this check.
     *
     * @param entity Instance of JPA Entity
     * @return false if entity's state has not been loaded or if the attribute state has not been loaded, else true
     */
    public boolean isLoaded(Object entity) {
        return AttributeLoadChecker.getInstance().isLoaded(entity, getName());
    }

    /**
     * Shows if a Getter is present by following the Java Beans pattern. For example, if an attribute call "price" is assumed that the getter is called "getPrice".
     *
     * @return true if Getter is present and false otherwise.
     */
    public boolean isGetterPresent() {
        return info.getPropertyReader() != null;
    }

    /**
     * Shows if a Setter is present by following the Java Beans pattern. For example, if an attribute call "price" is assumed that the setter is called "setPrice".
     *
     * @return true if Getter is present and false otherwise.
     */
    public boolean isSetterPresent() {
        return info.getPropertyWriter() != null;
    }

    /**
     * Shows whether the attribute has a Getter and Setter following the Java Beans pattern.
     *
     * @return true if both Getter and Setter are present or false if one of the two is missing.
     *
     * @see #isGetterPresent()
     * @see #isSetterPresent()
     */
    public boolean isGetterAndSetterPresent() {
        return isGetterPresent() && isSetterPresent();
    }

    /**
     * Calls the Getter of the attribute (if present) according to the Java Beans pattern.
     *
     * @param entity Instance of JPA Entity
     * @return Current attribute value
     */
    @SuppressWarnings("unchecked")
    public <T extends Exception> Object get(Object entity) throws T {
        try {
            return info.getPropertyReader().invoke(entity);
        } catch (InvocationTargetException e) {

            throw (T) e.getTargetException();

        } catch (IllegalAccessException | IllegalArgumentException e) {
            final String msg = MessageFormat.format("Could not call the getter of the \"{0}\" attribute of the declaring class \"{1}\"", getName(), getDeclaringClass().getName());
            throw new JpaPropertyAccessException(msg, e);
        }
    }

    /**
     * Calls the Setter of the attribute (if present) according to the Java Beans pattern.
     *
     * @param entity Instance of JPA Entity
     * @param value New attribute value
     */
    @SuppressWarnings("unchecked")
    public <T extends Exception> void set(Object entity, Object value) throws T {
        try {
            info.getPropertyWriter().invoke(entity, value);
        } catch (InvocationTargetException e) {

            throw (T) e.getTargetException();

        } catch (IllegalAccessException | IllegalArgumentException e) {
            final String msg = MessageFormat.format("Could not call the setter of the \"{0}\" attribute of the declaring class \"{1}\"", getName(), getDeclaringClass().getName());
            throw new JpaPropertyAccessException(msg, e);
        }
    }

    /**
     * Lists all annotations present in the attribute and getter method (if present)
     *
     * @return Lists of annotations found
     */
    public List<Annotation> getAnnotations() {
        return info.getAnnotations();
    }

    /**
     * Checks whether an annotation is present in the attribute
     *
     * @param clazz A java.lang.Class that represents an annotation class
     * @param <A> Generic type representing any Annotation.
     *
     * @return The annotation according to the requested type, if it is present in the attribute. Otherwise, return null.
     */
    public <A extends Annotation> A getAnnotation(Class<A> clazz) {
        return info.getAnnotation(clazz);
    }

    /**
     * Checks whether an annotation is present in the attribute
     *
     * @param clazz A java.lang.Class that represents an annotation class
     * @return true if the annotation is present in the attribute or false otherwise.
     */
    public boolean isAnnotationPresent(Class<? extends Annotation> clazz) {
        return info.getAnnotation(clazz) != null;
    }

    /**
     * Checks if any of the annotations provided are present in the attribute
     *
     * @param classes
     * @return true if any of the annotations are present in the attribute or false otherwise.
     */
    public boolean isAnyAnnotationPresent(Class<? extends Annotation>... classes) {
        for (Class<? extends Annotation> clazz : classes) {
            if (isAnnotationPresent(clazz)) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        return MessageFormat.format("{0}.{1} ({2})", getDeclaringClass().getName(), getName(), getType().getName());
    }

}
