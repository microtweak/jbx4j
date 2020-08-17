package com.github.microtweak.jbx4j.serializer.resolvers;

import com.github.microtweak.jbx4j.serializer.resolver.EntityResolver;
import com.github.microtweak.jbx4j.serializer.resolver.JpaEntityData;

import java.lang.annotation.Annotation;
import java.util.List;

public class StringEntityResolver implements EntityResolver<String> {

    @Override
    public int getOrdinal() {
        return 120;
    }

    @Override
    public String resolve(Object parent, Class<String> rawType, JpaEntityData<String> data, List<Annotation> annotations) {
        return "";
    }

}
