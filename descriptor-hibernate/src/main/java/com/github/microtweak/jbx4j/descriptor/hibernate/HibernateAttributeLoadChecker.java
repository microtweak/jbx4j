package com.github.microtweak.jbx4j.descriptor.hibernate;

import com.github.microtweak.jbx4j.descriptor.core.spi.AttributeLoadChecker;

import javax.persistence.Persistence;

public class HibernateAttributeLoadChecker extends AttributeLoadChecker {

    @Override
    public boolean isLoaded(Object entity, String attrName) {
        return Persistence.getPersistenceUtil().isLoaded(entity, attrName);
    }

}
