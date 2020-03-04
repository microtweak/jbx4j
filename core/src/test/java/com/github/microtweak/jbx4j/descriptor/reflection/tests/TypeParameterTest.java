package com.github.microtweak.jbx4j.descriptor.reflection.tests;

import com.github.microtweak.jbx4j.descriptor.attribute.CommonEntityAttribute;
import com.github.microtweak.jbx4j.descriptor.tags.ReflectionTest;
import com.github.microtweak.jbx4j.descriptor.reflection.beans.ParameterizedAttributes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ReflectionTest
public class TypeParameterTest extends BaseEntityAttributeAccessTest {

    @Test
    public void readAttributeWithoutTypeParameter() {
        CommonEntityAttribute attr = getEntityAttribute(ParameterizedAttributes.class, "withoutTypeParameter");

        assertAll(
            () -> assertNotNull(attr),
            () -> assertEquals(Long.class, attr.getType()),
            () -> assertTrue(attr.getTypeParameters().isEmpty())
        );
    }

    @ParameterizedTest
    @CsvSource({
        "java.util.List, singleTypeParameterList",
        "java.util.Set, singleTypeParameterSet"
    })
    public void readAttributeWithSingleTypeParameter(String attrType, String attrName) throws ClassNotFoundException {
        Class<?> type = Class.forName(attrType);
        CommonEntityAttribute attr = getEntityAttribute(ParameterizedAttributes.class, attrName);

        assertAll(
            () -> assertNotNull(attr),
            () -> assertEquals(type, attr.getType()),
            () -> assertIterableEquals(Arrays.asList(String.class), attr.getTypeParameters())
        );
    }

    @Test
    public void readAttributeWithPairTypeParameter() {
        CommonEntityAttribute attr = getEntityAttribute(ParameterizedAttributes.class, "pairTypeParameter");

        assertAll(
            () -> assertNotNull(attr),
            () -> assertEquals(Map.class, attr.getType()),
            () -> assertIterableEquals(Arrays.asList(Integer.class, String.class), attr.getTypeParameters())
        );
    }

}
