package com.github.microtweak.jbx4j.descriptor.core.reflection.tests;

import com.github.microtweak.jbx4j.descriptor.core.JpaPropertyInfo;
import com.github.microtweak.jbx4j.descriptor.core.attribute.CommonEntityAttribute;

import java.lang.reflect.Field;

public abstract class BaseEntityAttributeAccessTest {

    protected CommonEntityAttribute getEntityAttribute(Class<?> clazz, String fieldName) {
        try {
            Field attr = clazz.getDeclaredField(fieldName);
            return new CommonEntityAttribute(new JpaPropertyInfo(attr));
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

}
