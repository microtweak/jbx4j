package com.github.microtweak.jbx4j.serializer.resolvers;

import com.github.microtweak.jbx4j.serializer.resolver.EntityResolver;
import com.github.microtweak.jbx4j.serializer.resolver.JpaEntityData;

import java.lang.annotation.Annotation;
import java.util.List;

public class GenericEntityResolver implements EntityResolver<Object> {

    @Override
    public Object resolve(Object parent, Class<Object> rawType, JpaEntityData<Object> data, List<Annotation> annotations) {
        return new Object();
    }

}
