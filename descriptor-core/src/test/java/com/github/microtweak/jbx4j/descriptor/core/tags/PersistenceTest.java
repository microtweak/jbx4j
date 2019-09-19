package com.github.microtweak.jbx4j.descriptor.core.tags;

import eu.drus.jpa.unit.api.Cleanup;
import eu.drus.jpa.unit.api.JpaUnit;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Cleanup
@ExtendWith(JpaUnit.class)
@Tag("persistence")
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface PersistenceTest {
}
