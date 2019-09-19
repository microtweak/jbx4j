package com.github.microtweak.jbx4j.descriptor.core.persistence.tests;

import com.github.microtweak.jbx4j.descriptor.core.JpaDescriptor;
import com.github.microtweak.jbx4j.descriptor.core.attribute.OneToManyRelationshipEntityAttribute;
import com.github.microtweak.jbx4j.descriptor.core.persistence.entities.Author;
import com.github.microtweak.jbx4j.descriptor.core.persistence.entities.Book;
import com.github.microtweak.jbx4j.descriptor.core.tags.PersistenceTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@PersistenceTest
public class OneToManyRelationshipEntityAttributeFetchTest extends BasePersistenceTest implements RelationshipEntityAttributeFetchTest {

    @ParameterizedTest(name = "With clear: {0}")
    @ValueSource(strings = { "true", "false" })
    public void fetchLazy(boolean clearPersistenceContext) {
        saveAuthorAndBooks(clearPersistenceContext);

        Author author = getEntityManager()
                .createQuery("SELECT a FROM Author a", Author.class)
                .getSingleResult();

        OneToManyRelationshipEntityAttribute attr = (OneToManyRelationshipEntityAttribute) JpaDescriptor.of(Author.class)
                .getAttribute("books");

        assertNotNull(attr);

        if (clearPersistenceContext) {
            assertFalse(attr.isLoaded(author));
        } else {
            assertTrue(attr.isLoaded(author));
        }
    }


    @ParameterizedTest(name = "With clear: {0}")
    @ValueSource(strings = { "true", "false" })
    public void fetchEager(boolean clearPersistenceContext) {
        saveAuthorAndBooks(clearPersistenceContext);

        Author author = getEntityManager()
                .createQuery("SELECT DISTINCT a FROM Author a LEFT JOIN FETCH a.books", Author.class)
                .getSingleResult();

        OneToManyRelationshipEntityAttribute attr = (OneToManyRelationshipEntityAttribute) JpaDescriptor.of(Author.class)
                .getAttribute("books");

        assertAll(
            () -> assertNotNull(attr),
            () -> assertTrue(attr.isLoaded(author))
        );
    }

    private void saveAuthorAndBooks(boolean clearPersistenceContext) {
        Author author = new Author();
        author.setName("J.K. Rowling");

        save(author);

        Book book = new Book();
        book.setTitle("Harry Potter and the Philosopher's Stone");
        book.setIsbn("0747532699");
        book.setPublishYear(1997);
        book.setAuthor(author);

        save(book);

        book = new Book();
        book.setTitle("Harry Potter and the Chamber of Secrets");
        book.setIsbn("0747538492");
        book.setPublishYear(1998);
        book.setAuthor(author);

        save(book);

        book = new Book();
        book.setTitle("Harry Potter and the Prisoner of Azkaban");
        book.setIsbn("0747542155");
        book.setPublishYear(1999);
        book.setAuthor(author);

        save(book);

        if (clearPersistenceContext) {
            getEntityManager().clear();
        }
    }

}
