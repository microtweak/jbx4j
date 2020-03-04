package com.github.microtweak.jbx4j.descriptor.persistence.tests;

import com.github.microtweak.jbx4j.descriptor.JpaDescriptor;
import com.github.microtweak.jbx4j.descriptor.attribute.CommonEntityAttribute;
import com.github.microtweak.jbx4j.descriptor.attribute.ManyToOneRelationshipEntityAttribute;
import com.github.microtweak.jbx4j.descriptor.persistence.entities.Address;
import com.github.microtweak.jbx4j.descriptor.persistence.entities.Country;
import com.github.microtweak.jbx4j.descriptor.persistence.entities.Customer;
import com.github.microtweak.jbx4j.descriptor.tags.PersistenceTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@PersistenceTest
public class CommonEntityAttributeFetchTest extends BasePersistenceTest {

    @ParameterizedTest(name = "With clear: {0}")
    @ValueSource(strings = { "true", "false" })
    public void fetchBasicEntityAttribute(boolean clearPersistenceContext) {
        Country entity = new Country();
        entity.setName("Brazil");
        entity.setLanguage("Portuguese");

        save(entity);

        if (clearPersistenceContext) {
            getEntityManager().clear();
        }

        entity = getEntityManager()
                .createQuery("SELECT c FROM Country c WHERE c.name = :name", Country.class)
                .setParameter("name", entity.getName())
                .getSingleResult();

        CommonEntityAttribute nameAttr = (CommonEntityAttribute) JpaDescriptor.of(Country.class)
                .getAttribute("name");

        assertNotNull(nameAttr);
        assertTrue(nameAttr.isLoaded(entity));
    }

    @ParameterizedTest(name = "With clear: {0}")
    @ValueSource(strings = { "true", "false" })
    public void fetchEmbeddedWithLazyEntityAttribute(boolean clearPersistenceContext) {
        saveCustomer(clearPersistenceContext);

        Customer customer = getEntityManager()
                .createQuery("SELECT c FROM Customer c WHERE c.firstName = :name", Customer.class)
                .setParameter("name", "Harry")
                .getSingleResult();

        CommonEntityAttribute addressAttr = (CommonEntityAttribute) JpaDescriptor.of(Customer.class)
                .getAttribute("address");

        assertAll(
            () -> assertNotNull(addressAttr),
            () -> assertTrue(addressAttr.isLoaded(customer))
        );

        ManyToOneRelationshipEntityAttribute countryAttr = (ManyToOneRelationshipEntityAttribute) JpaDescriptor.of(Address.class)
                .getAttribute("country");

        assertNotNull(countryAttr);

        if (clearPersistenceContext) {
            assertFalse(countryAttr.isLoaded(customer.getAddress()));
        } else {
            assertTrue(countryAttr.isLoaded(customer.getAddress()));
        }
    }

    @ParameterizedTest(name = "With clear: {0}")
    @ValueSource(strings = { "true", "false" })
    public void fetchEmbeddedWithEagerEntityAttribute(boolean clearPersistenceContext) {
        saveCustomer(clearPersistenceContext);

        Customer customer = getEntityManager()
                .createQuery("SELECT c FROM Customer c LEFT JOIN FETCH c.address.country WHERE c.firstName = :name", Customer.class)
                .setParameter("name", "Harry")
                .getSingleResult();

        CommonEntityAttribute addressAttr = (CommonEntityAttribute) JpaDescriptor.of(Customer.class)
                .getAttribute("address");

        assertAll(
            () -> assertNotNull(addressAttr),
            () -> assertTrue(addressAttr.isLoaded(customer))
        );

        ManyToOneRelationshipEntityAttribute countryAttr = (ManyToOneRelationshipEntityAttribute) JpaDescriptor.of(Address.class)
                .getAttribute("country");

        assertAll(
            () -> assertNotNull(countryAttr),
            () -> assertTrue(countryAttr.isLoaded(customer.getAddress()))
        );
    }

    private void saveCustomer(boolean clearPersistenceContext) {
        Country country = new Country();
        country.setName("England");
        country.setLanguage("English");

        save(country);

        Customer customer = new Customer();
        customer.setFirstName("Harry");
        customer.setLastName("Potter");

        Address address = new Address();
        address.setStreet("4 Privet Drive");
        address.setCity("Little Whinging");
        address.setState("Surrey");
        address.setCountry(country);
        customer.setAddress(address);

        save(customer);

        if (clearPersistenceContext) {
            getEntityManager().clear();
        }
    }

}