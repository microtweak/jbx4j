package com.github.microtweak.jbx4j.descriptor.persistence.tests;

import com.github.microtweak.jbx4j.descriptor.JpaDescriptor;
import com.github.microtweak.jbx4j.descriptor.attribute.ManyToManyRelationshipEntityAttribute;
import com.github.microtweak.jbx4j.descriptor.persistence.entities.Post;
import com.github.microtweak.jbx4j.descriptor.persistence.entities.Tag;
import com.github.microtweak.jbx4j.descriptor.tags.PersistenceTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@PersistenceTest
public class ManyToManyRelationshipEntityAttributeFetchTest extends BasePersistenceTest implements RelationshipEntityAttributeFetchTest {

    @ParameterizedTest(name = "With clear: {0}")
    @ValueSource(strings = { "true", "false" })
    public void fetchLazy(boolean clearPersistenceContext) {
        saveTagsAndPost(clearPersistenceContext);

        Post post = getEntityManager()
                .createQuery("SELECT p FROM Post p", Post.class)
                .getSingleResult();

        ManyToManyRelationshipEntityAttribute attr = (ManyToManyRelationshipEntityAttribute) JpaDescriptor.of(Post.class)
                .getAttribute("tags");

        assertNotNull(attr);

        if (clearPersistenceContext) {
            assertFalse(attr.isLoaded(post));
        } else {
            assertTrue(attr.isLoaded(post));
        }
    }

    @ParameterizedTest(name = "With clear: {0}")
    @ValueSource(strings = { "true", "false" })
    public void fetchEager(boolean clearPersistenceContext) {
        saveTagsAndPost(clearPersistenceContext);

        Post post = getEntityManager()
                .createQuery("SELECT DISTINCT p FROM Post p LEFT JOIN FETCH p.tags", Post.class)
                .getSingleResult();

        ManyToManyRelationshipEntityAttribute attr = (ManyToManyRelationshipEntityAttribute) JpaDescriptor.of(Post.class)
                .getAttribute("tags");

        assertAll(
            () -> assertNotNull(attr),
            () -> assertTrue(attr.isLoaded(post))
        );
    }

    private void saveTagsAndPost(boolean clearPersistenceContext) {
        Tag firstTag = new Tag();
        firstTag.setName("Development");

        save(firstTag);

        Tag secondTag = new Tag();
        secondTag.setName("Database");

        save(secondTag);

        Post post = new Post();
        post.setPostedAt(new Date());
        post.setTitle("Lorem ipsum fusce ultricies eget felis");
        post.setContent(
            "Lorem ipsum fusce ultricies eget felis quisque justo himenaeos pellentesque, " +
            "turpis inceptos fringilla amet massa ipsum quisque potenti, condimentum venenatis convallis"
        );

        post.getTags().add(firstTag);
        post.getTags().add(secondTag);

        save(post);

        if (clearPersistenceContext) {
            getEntityManager().clear();
        }
    }

}
