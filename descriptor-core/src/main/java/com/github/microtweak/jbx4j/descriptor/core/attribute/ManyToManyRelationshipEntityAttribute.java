package com.github.microtweak.jbx4j.descriptor.core.attribute;

import com.github.microtweak.jbx4j.descriptor.core.JpaPropertyInfo;

import javax.persistence.CascadeType;
import javax.persistence.ManyToMany;

/**
 * Concrete implementation of {@link RelationshipEntityAttribute} for {@link ManyToMany} relationship
 *
 * @author Marcos Koch Salvador
 * @since 1.0.0
 *
 * @see RelationshipEntityAttribute
 * @see EntityAttribute
 * @see ManyToMany
 */
public class ManyToManyRelationshipEntityAttribute extends RelationshipEntityAttribute<ManyToMany> {

    public ManyToManyRelationshipEntityAttribute(JpaPropertyInfo info, ManyToMany manyToMany) {
        super(info, manyToMany);
    }

    @Override
    public CascadeType[] getCascadeTypes() {
        return getRelationshipAnnotation().cascade();
    }

}
