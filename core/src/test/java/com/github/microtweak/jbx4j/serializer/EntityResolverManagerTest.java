package com.github.microtweak.jbx4j.serializer;

import com.github.microtweak.jbx4j.serializer.resolver.EntityResolver;
import com.github.microtweak.jbx4j.serializer.resolver.EntityResolverManager;
import com.github.microtweak.jbx4j.serializer.resolvers.CharSeqEntityResolver;
import com.github.microtweak.jbx4j.serializer.resolvers.GenericEntityResolver;
import com.github.microtweak.jbx4j.serializer.resolvers.StringEntityResolver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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
        EntityResolver<?> er = manager.lookupResolver(null, Long.class, emptyList());
        assertEquals(GenericEntityResolver.class, er.getClass());
    }

    @Test
    public void lookupResolverByMediumOrdinal() {
        EntityResolver<?> er = manager.lookupResolver(null, StringBuilder.class, emptyList());
        assertEquals(CharSeqEntityResolver.class, er.getClass());
    }

    @Test
    public void lookupResolverByHighestOrdinal() {
        EntityResolver<?> er = manager.lookupResolver(null, String.class, emptyList());
        assertEquals(StringEntityResolver.class, er.getClass());
    }

}