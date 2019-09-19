package com.github.microtweak.jbx4j.descriptor.core.persistence.tests;

import com.github.microtweak.jbx4j.descriptor.core.JpaDescriptor;
import com.github.microtweak.jbx4j.descriptor.core.persistence.entities.Customer;
import com.github.microtweak.jbx4j.descriptor.core.tags.PersistenceTest;
import eu.drus.jpa.unit.api.Cleanup;
import eu.drus.jpa.unit.api.CleanupPhase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@PersistenceTest
@Cleanup(phase = CleanupPhase.NONE)
public class LookupDescriptorTest extends BasePersistenceTest {

    private static boolean populated;

    @Test
    public void lookupDescriptorByRealEntity() {
        insertIfNecessary();

        Customer entity = getEntityManager().find(Customer.class, 1L);

        assertDescriptor(entity);
    }

    @Test
    public void lookupDescriptorByProxy() {
        insertIfNecessary();

        Customer entity = getEntityManager().getReference(Customer.class, 1L);

        assertDescriptor(entity);
    }

    private void assertDescriptor(Customer entity) {
        JpaDescriptor descriptor = JpaDescriptor.of(entity.getClass());

        assertAll(
            () -> assertNotNull(descriptor),
            () -> assertEquals(Customer.class, descriptor.getJpaClass())
        );
    }

    private void insertIfNecessary() {
        if (populated) {
            return;
        }

        Customer customer = new Customer();
        customer.setFirstName("Harry");
        customer.setLastName("Potter");

        customer.getUser().setUsername("harrypotter");
        customer.getUser().setPassword("h98$1y%p007#");

        save(customer);

        getEntityManager().clear();

        populated = true;
    }

}
