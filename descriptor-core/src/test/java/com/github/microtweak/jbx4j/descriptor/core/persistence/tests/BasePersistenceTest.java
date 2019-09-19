package com.github.microtweak.jbx4j.descriptor.core.persistence.tests;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceProperty;

public class BasePersistenceTest {

    @PersistenceContext(unitName = "JpaDescriptorTest", properties = {
        @PersistenceProperty(name = "javax.persistence.provider", value = "${jpa.persistence.provider}")
    })
    private EntityManager entityManager;

    protected void save(Object entity) {
        getEntityManager().persist(entity);
        getEntityManager().flush();
    }

    protected EntityManager getEntityManager() {
        return entityManager;
    }

}
