package com.github.microtweak.jbx4j.descriptor.reflection.tests;

import com.github.microtweak.jbx4j.descriptor.attribute.CommonEntityAttribute;
import com.github.microtweak.jbx4j.descriptor.reflection.beans.SimpleAttributes;
import com.github.microtweak.jbx4j.descriptor.tags.ReflectionTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@ReflectionTest
public class GetterAndSetterAccessTest extends BaseEntityAttributeAccessTest {

    @ParameterizedTest
    @ValueSource(strings = { "byte", "short", "int", "long", "float", "double", "boolean", "char" })
    public void accessPrimitiveAndWrapperTypeProperties(String typeName) {
        CommonEntityAttribute primitiveAttr = getEntityAttribute(SimpleAttributes.class, typeName + "Primitive");

        assertAll("Primitive attribute of \"" + typeName + "\"",
            () -> assertNotNull(primitiveAttr),
            () -> assertTrue(primitiveAttr.isGetterPresent()),
            () -> assertTrue(primitiveAttr.isSetterPresent())
        );

        CommonEntityAttribute wrapperAttr = getEntityAttribute(SimpleAttributes.class, typeName + "Wrapper");

        assertAll("Wrapper attribute of \"" + typeName + "\"",
            () -> assertNotNull(wrapperAttr),
            () -> assertTrue(wrapperAttr.isGetterPresent()),
            () -> assertTrue(wrapperAttr.isSetterPresent())
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "text", "utilDate", "sqlDate", "sqlTime", "sqlTimestamp", "localDate", "localTime", "localDateTime", "instant"
    })
    public void accessTextAndDateTypeProperties(String typeName) {
        CommonEntityAttribute attr = getEntityAttribute(SimpleAttributes.class, typeName + "Attr");

        assertAll("Object attribute of \"" + typeName + "\"",
            () -> assertNotNull(attr),
            () -> assertTrue(attr.isGetterPresent()),
            () -> assertTrue(attr.isSetterPresent())
        );
    }

}
