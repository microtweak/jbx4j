package com.github.microtweak.jbx4j.descriptor.core.persistence.tests;

import com.github.microtweak.jbx4j.descriptor.core.JpaDescriptor;
import com.github.microtweak.jbx4j.descriptor.core.attribute.ManyToOneRelationshipEntityAttribute;
import com.github.microtweak.jbx4j.descriptor.core.persistence.entities.Author;
import com.github.microtweak.jbx4j.descriptor.core.persistence.entities.Book;
import com.github.microtweak.jbx4j.descriptor.core.tags.PersistenceTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@PersistenceTest
public class ManyToOneRelationshipEntityAttributeFetchTest extends BasePersistenceTest implements RelationshipEntityAttributeFetchTest {

    @ParameterizedTest(name = "With clear: {0}")
    @ValueSource(strings = { "true", "false" })
    public void fetchLazy(boolean clearPersistenceContext) {
        saveAuthorAndBook(clearPersistenceContext);

        Book book = getEntityManager()
                .createQuery("SELECT b FROM Book b", Book.class)
                .getSingleResult();

        ManyToOneRelationshipEntityAttribute attr = (ManyToOneRelationshipEntityAttribute) JpaDescriptor.of(Book.class)
                .getAttribute("author");

        assertNotNull(attr);

        if (clearPersistenceContext) {
            assertFalse(attr.isLoaded(book));
        } else {
            assertTrue(attr.isLoaded(book));
        }
    }


    @ParameterizedTest(name = "With clear: {0}")
    @ValueSource(strings = { "true", "false" })
    public void fetchEager(boolean clearPersistenceContext) {
        saveAuthorAndBook(clearPersistenceContext);

        Book book = getEntityManager()
                .createQuery("SELECT b FROM Book b LEFT JOIN FETCH b.author", Book.class)
                .getSingleResult();

        ManyToOneRelationshipEntityAttribute attr = (ManyToOneRelationshipEntityAttribute) JpaDescriptor.of(Book.class)
                .getAttribute("author");

        assertAll(
            () -> assertNotNull(attr),
            () -> assertTrue(attr.isLoaded(book))
        );
    }

    private void saveAuthorAndBook(boolean clearPersistenceContext) {
        Author author = new Author();
        author.setName("J.K. Rowling");

        save(author);

        Book book = new Book();
        book.setTitle("Harry Potter and the Philosopher's Stone");
        book.setIsbn("0747532699");
        book.setPublishYear(1997);
        book.setAuthor(author);

        save(book);

        if (clearPersistenceContext) {
            getEntityManager().clear();
        }
    }

}
