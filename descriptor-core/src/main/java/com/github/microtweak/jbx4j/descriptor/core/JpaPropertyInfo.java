package com.github.microtweak.jbx4j.descriptor.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Object with information about properties and methods for writing/reading on these properties.
 *
 * @author Marcos Koch Salvador
 * @since 1.0.0
 */
public class JpaPropertyInfo {

    private static final Logger LOG = Logger.getLogger(JpaPropertyInfo.class.getName());

    private Field field;
    private Method propertyReader;
    private Method propertyWriter;

    private final Map<Class<?>, Annotation> annotations = new HashMap<>();

    public JpaPropertyInfo(Field field) {
        this.field = field;

        Stream.of(field.getAnnotations()).forEach(a -> annotations.put(a.annotationType(), a));

        Class<?> declaredAt = field.getDeclaringClass();
        String name = field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);

        try {
            String prefix = (field.getType().equals(boolean.class) ? "is" : "get");
            propertyReader = declaredAt.getMethod(prefix + name);

            Stream.of(propertyReader.getAnnotations()).forEach(a -> annotations.put(a.annotationType(), a));
        } catch (NoSuchMethodException e) {
            final String msg = MessageFormat.format(
                    "The getter of the \"{0}\" attribute was not found in the declaring class \"{1}\"",
                    field.getName(), declaredAt.getName()
            );
            LOG.log(Level.FINEST, msg);
        }

        try {
            propertyWriter = declaredAt.getMethod("set" + name, field.getType());
        } catch (NoSuchMethodException e) {
            final String msg = MessageFormat.format(
                    "The setter of the \"{0}\" attribute was not found in the declaring class \"{1}\"",
                    field.getName(), declaredAt.getName()
            );
            LOG.log(Level.FINEST, msg);
        }
    }

    public Field getField() {
        return field;
    }

    public Method getPropertyReader() {
        return propertyReader;
    }

    public Method getPropertyWriter() {
        return propertyWriter;
    }

    public List<Annotation> getAnnotations() {
        return Collections.unmodifiableList(new ArrayList<>(annotations.values()));
    }

    public <A extends Annotation> A getAnnotation(Class<?> clazz) {
        return (A) annotations.get(clazz);
    }
}
