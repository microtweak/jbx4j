package com.github.microtweak.jbx4j.descriptor.persistence.tests;

public interface RelationshipEntityAttributeFetchTest {

    void fetchLazy(boolean clearPersistenceContext);

    void fetchEager(boolean clearPersistenceContext);

}
