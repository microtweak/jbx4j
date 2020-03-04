package com.github.microtweak.jbx4j.descriptor.hibernate;

import com.github.microtweak.jbx4j.descriptor.spi.ReflectionHelper;
import org.hibernate.proxy.HibernateProxy;

public class HibernateReflectionHelper extends ReflectionHelper {

    @Override
    public boolean isEntity(Class<?> clazz) {
        if (super.isEntity(clazz)) {
            return true;
        }
        return HibernateProxy.class.isAssignableFrom(clazz);
    }

    @Override
    public Class<?> getJpaClass(Class<?> proxy) {
        if (HibernateProxy.class.isAssignableFrom(proxy)) {
            return proxy.getSuperclass();
        }
        return proxy;
    }

}
