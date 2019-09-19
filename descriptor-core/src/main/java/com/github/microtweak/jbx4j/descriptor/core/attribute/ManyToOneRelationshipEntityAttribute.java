package com.github.microtweak.jbx4j.descriptor.core.attribute;

import com.github.microtweak.jbx4j.descriptor.core.JpaPropertyInfo;

import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;

/**
 * Concrete implementation of {@link RelationshipEntityAttribute} for {@link ManyToOne} relationship
 *
 * @author Marcos Koch Salvador
 * @since 1.0.0
 *
 * @see RelationshipEntityAttribute
 * @see EntityAttribute
 * @see ManyToOne
 */
public class ManyToOneRelationshipEntityAttribute extends RelationshipEntityAttribute<ManyToOne> {

    public ManyToOneRelationshipEntityAttribute(JpaPropertyInfo info, ManyToOne manyToOne) {
        super(info, manyToOne);
    }

    @Override
    public CascadeType[] getCascadeTypes() {
        return getRelationshipAnnotation().cascade();
    }

}
