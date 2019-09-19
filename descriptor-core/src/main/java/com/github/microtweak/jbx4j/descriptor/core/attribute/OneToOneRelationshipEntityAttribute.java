package com.github.microtweak.jbx4j.descriptor.core.attribute;

import com.github.microtweak.jbx4j.descriptor.core.JpaPropertyInfo;

import javax.persistence.CascadeType;
import javax.persistence.OneToOne;

/**
 * Concrete implementation of {@link RelationshipEntityAttribute} for {@link OneToOne} relationship
 *
 * @author Marcos Koch Salvador
 * @since 1.0.0
 *
 * @see RelationshipEntityAttribute
 * @see EntityAttribute
 * @see OneToOne
 */
public class OneToOneRelationshipEntityAttribute extends RelationshipEntityAttribute<OneToOne> {

    public OneToOneRelationshipEntityAttribute(JpaPropertyInfo info, OneToOne oneToOne) {
        super(info, oneToOne);
    }

    @Override
    public CascadeType[] getCascadeTypes() {
        return getRelationshipAnnotation().cascade();
    }

}
