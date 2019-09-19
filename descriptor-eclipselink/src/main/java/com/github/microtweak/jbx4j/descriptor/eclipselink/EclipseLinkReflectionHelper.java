package com.github.microtweak.jbx4j.descriptor.eclipselink;

import com.github.microtweak.jbx4j.descriptor.core.spi.ReflectionHelper;

import java.lang.reflect.Field;

public class EclipseLinkReflectionHelper extends ReflectionHelper {

    @Override
    public boolean isEntityProperty(Field field) {
        return !field.getName().startsWith(EclipseLinkConstants.WEAVING_PREFIX);
    }

}
