package com.github.microtweak.jbx4j.serializer;

import com.github.microtweak.jbx4j.descriptor.persistence.entities.Author;
import com.github.microtweak.jbx4j.descriptor.persistence.entities.Book;
import com.github.microtweak.jbx4j.descriptor.persistence.entities.Employee;
import org.junit.jupiter.api.Test;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CircularReferenceDetectorTest {

    @Test
    public void singlePropertyAllowed() {
        CircularReferenceDetector detector = new CircularReferenceDetector().add(Book.class);

        assertToOneSerializationAllowed(detector, Author.class);

        detector = detector.add(Author.class);

        assertToManySerializationDenied(detector, Book.class);
    }

    @Test
    public void collectionPropertyAllowed() {
        CircularReferenceDetector detector = new CircularReferenceDetector().add(Author.class);

        assertToManySerializationAllowed(detector, Book.class);

        detector = detector.add(Book.class);

        assertToOneSerializationDenied(detector, Author.class);
    }

    @Test
    public void circularRelationshipSinglePropertyAllowed() {
        CircularReferenceDetector detector = new CircularReferenceDetector().add(Employee.class);

        assertToOneSerializationAllowed(detector, Employee.class);

        detector = detector.add(Employee.class);

        assertToOneSerializationDenied(detector, Employee.class);
    }

    //@Test
    public void circularRelationshipCollectionPropertyAllowed() {
        CircularReferenceDetector detector = new CircularReferenceDetector().add(Employee.class);

        assertToManySerializationAllowed(detector, Employee.class);

        detector = detector.add(Employee.class);

        assertToManySerializationDenied(detector, Employee.class);
    }

    private void assertToOneSerializationAllowed(CircularReferenceDetector detector, Class<?> forType) {
        assertFalse( detector.containsExceptLastVisited(forType) );
    }

    private void assertToOneSerializationDenied(CircularReferenceDetector detector, Class<?> forType) {
        assertTrue( detector.containsExceptLastVisited(forType) );
    }

    private void assertToManySerializationAllowed(CircularReferenceDetector detector, Class<?> forType) {
        assertFalse( detector.containsAny(singletonList(forType)) );
    }

    private void assertToManySerializationDenied(CircularReferenceDetector detector, Class<?> forType) {
        assertTrue( detector.containsAny(singletonList(forType)) );
    }

}
