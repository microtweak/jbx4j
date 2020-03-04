package com.github.microtweak.jbx4j.serializer;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Utility to detect circular reference between classes.
 *
 * @author Marcos Koch Salvador
 * @since 1.0.0
 */
public class CircularReferenceDetector {

    private Set<Class<?>> classReferences;

    public CircularReferenceDetector() {
        classReferences = new LinkedHashSet<>();
    }

    private CircularReferenceDetector(Set<Class<?>> classReferences) {
        this();
        this.classReferences.addAll(classReferences);
    }

    /**
     * Adds a class already visited
     *
     * @param clazz Class visited
     * @return true
     */
    public CircularReferenceDetector add(Class<?> clazz) {
        CircularReferenceDetector ep = new CircularReferenceDetector(this.classReferences);
        ep.classReferences.add(clazz);
        return ep;
    }

    /**
     * Checks if the class has already been visited
     *
     * @param clazz Class to be checked
     * @return true if the class has already been visited or false otherwise.
     */
    public boolean contains(Class<?> clazz) {
        return classReferences.contains(clazz);
    }

    /**
     * Checks if elements of a class list have already been visited
     *
     * @param classes Class to be checked
     * @return true if any class has already been visited or false otherwise.
     */
    public boolean contains(List<Class<?>> classes) {
        for (Class<?> clazz : classes) {
            if (contains(clazz)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return classReferences.stream().map(cls -> cls.getName()).collect(Collectors.joining(" => "));
    }

}
