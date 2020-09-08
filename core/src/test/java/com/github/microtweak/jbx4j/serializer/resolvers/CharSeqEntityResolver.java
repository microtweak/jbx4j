package com.github.microtweak.jbx4j.serializer.resolvers;

import com.github.microtweak.jbx4j.serializer.resolver.EntityResolver;
import com.github.microtweak.jbx4j.serializer.resolver.JpaEntityData;

import java.lang.annotation.Annotation;
import java.util.List;

public class CharSeqEntityResolver implements EntityResolver<CharSequence> {

    @Override
    public int getOrdinal() {
        return 110;
    }

    @Override
    public CharSequence resolve(Object parent, Class<CharSequence> rawType, JpaEntityData<CharSequence> data, List<Annotation> annotations) {
        return "";
    }

}
