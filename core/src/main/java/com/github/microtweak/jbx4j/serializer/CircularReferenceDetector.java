package com.github.microtweak.jbx4j.serializer;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Utility to detect circular reference between classes.
 *
 * @author Marcos Koch Salvador
 * @since 1.0.0
 */
public class CircularReferenceDetector {

    private Map<Class<?>, Integer> classReferences;
    private Class<?> lastVisited;

    public CircularReferenceDetector() {
        classReferences = new LinkedHashMap<>();
    }

    private CircularReferenceDetector(Map<Class<?>, Integer> parentReferences, Class<?> lastVisited) {
        classReferences = new LinkedHashMap<>( parentReferences );
        classReferences.merge(lastVisited, 1, (count, increment) -> count + increment);

        this.lastVisited = lastVisited;
    }

    /**
     * Adds a class already visited
     *
     * @param clazz Class visited
     * @return true
     */
    public CircularReferenceDetector add(Class<?> clazz) {
        return new CircularReferenceDetector(this.classReferences, clazz);
    }

    /**
     * Checks if the class has already been visited
     *
     * @param clazz Class to be checked
     * @return true if the class has already been visited or false otherwise.
     */
    public boolean contains(Class<?> clazz) {
        return classReferences.containsKey(clazz);
    }

    /**
     * Checks if elements of a class list have already been visited
     *
     * @param classes Classes to be checked
     * @return true if any class has already been visited or false otherwise.
     */
    public boolean containsAny(Collection<Class<?>> classes) {
        for (Class<?> clazz : classes) {
            if (contains(clazz)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the class has already been visited, ignoring the last visited class
     *
     * @param clazz Class to be checked
     * @return true if the class has already been visited or false otherwise.
     */
    public boolean containsExceptLastVisited(Class<?> clazz) {
        if (lastVisited != null && lastVisited.equals(clazz)) {
            return classReferences.getOrDefault(clazz, 0) > 1;
        }
        return contains(clazz);
    }

    @Override
    public String toString() {
        return classReferences.keySet().stream().map(cls -> cls.getName()).collect(Collectors.joining(" => "));
    }

}
