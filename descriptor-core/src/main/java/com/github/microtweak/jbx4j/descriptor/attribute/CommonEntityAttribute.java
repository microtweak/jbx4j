package com.github.microtweak.jbx4j.descriptor.attribute;


import com.github.microtweak.jbx4j.descriptor.JpaPropertyInfo;

/**
 * Implementation of EntityAttribute for common fields (primitive types, wrapper types, String, Date Java, Java Time, etc.)
 *
 * @author Marcos Koch Salvador
 * @since 1.0.0
 */
public class CommonEntityAttribute extends EntityAttribute {

    public CommonEntityAttribute(JpaPropertyInfo info) {
        super(info);
    }

}