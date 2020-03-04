package com.github.microtweak.jbx4j.serializer;

import com.github.microtweak.jbx4j.serializer.resolver.EntityResolver;
import com.github.microtweak.jbx4j.serializer.resolver.EntityResolverManager;
import com.github.microtweak.jbx4j.serializer.resolver.JpaEntityData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EntityResolverManagerTest {

    private static EntityResolverManager manager;

    @BeforeAll
    public static void prepareTest() {
        manager = new EntityResolverManager();
        manager.add(new GenericEntityResolver());
        manager.add(new CharSeqEntityResolver());
        manager.add(new StringEntityResolver());
    }

    @Test
    public void lookupResolverDefault() {
        EntityResolver<?> er = manager.lookupResolver(null, Long.class, new FakeJpaEntityData<>(), emptyList());
        assertEquals(GenericEntityResolver.class, er.getClass());
    }

    @Test
    public void lookupResolverByMediumOrdinal() {
        EntityResolver<?> er = manager.lookupResolver(null, StringBuilder.class, new FakeJpaEntityData<>(), emptyList());
        assertEquals(CharSeqEntityResolver.class, er.getClass());
    }

    @Test
    public void lookupResolverByHighestOrdinal() {
        EntityResolver<?> er = manager.lookupResolver(null, String.class, new FakeJpaEntityData<>(), emptyList());
        assertEquals(StringEntityResolver.class, er.getClass());
    }

}

class GenericEntityResolver implements EntityResolver<Object> {

    @Override
    public boolean canResolve(Object parent, Class<Object> rawType, JpaEntityData<Object> data, List<Annotation> annotations) {
        return true;
    }

    @Override
    public Object resolve(Object parent, Class<Object> rawType, JpaEntityData<Object> data, List<Annotation> annotations) {
        return new Object();
    }

}

class CharSeqEntityResolver implements EntityResolver<CharSequence> {

    @Override
    public int getOrdinal() {
        return 110;
    }

    @Override
    public boolean canResolve(Object parent, Class<CharSequence> rawType, JpaEntityData<CharSequence> data, List<Annotation> annotations) {
        return CharSequence.class.isAssignableFrom(rawType);
    }

    @Override
    public CharSequence resolve(Object parent, Class<CharSequence> rawType, JpaEntityData<CharSequence> data, List<Annotation> annotations) {
        return "";
    }

}

class StringEntityResolver implements EntityResolver<String> {

    @Override
    public int getOrdinal() {
        return 120;
    }

    @Override
    public boolean canResolve(Object parent, Class<String> rawType, JpaEntityData<String> data, List<Annotation> annotations) {
        return String.class.isAssignableFrom(rawType);
    }

    @Override
    public String resolve(Object parent, Class<String> rawType, JpaEntityData<String> data, List<Annotation> annotations) {
        return "";
    }

}

class FakeJpaEntityData<E> implements JpaEntityData<E> {

    @Override
    public Object get(String attrName) {
        return null;
    }

}
