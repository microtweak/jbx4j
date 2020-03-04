package com.github.microtweak.jbx4j.descriptor.persistence.tests;

import com.github.microtweak.jbx4j.descriptor.JpaDescriptor;
import com.github.microtweak.jbx4j.descriptor.attribute.OneToOneRelationshipEntityAttribute;
import com.github.microtweak.jbx4j.descriptor.persistence.entities.Customer;
import com.github.microtweak.jbx4j.descriptor.tags.PersistenceTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@PersistenceTest
public class OneToOneRelationshipEntityAttributeFetchTest extends BasePersistenceTest implements RelationshipEntityAttributeFetchTest {

    @ParameterizedTest(name = "With clear: {0}")
    @ValueSource(strings = { "true", "false" })
    public void fetchLazy(boolean clearPersistenceContext) {
        saveCustomerAndUser(clearPersistenceContext);

        Customer customer = getEntityManager()
                .createQuery("SELECT c FROM Customer c", Customer.class)
                .getSingleResult();

        OneToOneRelationshipEntityAttribute attr = (OneToOneRelationshipEntityAttribute) JpaDescriptor.of(Customer.class)
                .getAttribute("user");

        assertNotNull(attr);

        if (clearPersistenceContext) {
            assertFalse(attr.isLoaded(customer));
        } else {
            assertTrue(attr.isLoaded(customer));
        }
    }

    @ParameterizedTest(name = "With clear: {0}")
    @ValueSource(strings = { "true", "false" })
    public void fetchEager(boolean clearPersistenceContext) {
        saveCustomerAndUser(clearPersistenceContext);

        Customer customer = getEntityManager()
                .createQuery("SELECT c FROM Customer c LEFT JOIN FETCH c.user", Customer.class)
                .getSingleResult();

        OneToOneRelationshipEntityAttribute attr = (OneToOneRelationshipEntityAttribute) JpaDescriptor.of(Customer.class)
                .getAttribute("user");

        assertNotNull(attr);

        assertAll(
            () -> assertNotNull(attr),
            () -> assertTrue(attr.isLoaded(customer))
        );
    }

    private void saveCustomerAndUser(boolean clearPersistenceContext) {
        Customer customer = new Customer();
        customer.setFirstName("Harry");
        customer.setLastName("Potter");

        customer.getUser().setUsername("harrypotter");
        customer.getUser().setPassword("h98$1y%p007#");

        save(customer);

        if (clearPersistenceContext) {
            getEntityManager().clear();
        }
    }

}
