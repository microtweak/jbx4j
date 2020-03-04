package com.github.microtweak.jbx4j.descriptor.attribute;

import com.github.microtweak.jbx4j.descriptor.JpaPropertyInfo;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;

/**
 * Concrete implementation of {@link RelationshipEntityAttribute} for {@link OneToMany} relationship
 *
 * @author Marcos Koch Salvador
 * @since 1.0.0
 *
 * @see RelationshipEntityAttribute
 * @see EntityAttribute
 * @see OneToMany
 */
public class OneToManyRelationshipEntityAttribute extends RelationshipEntityAttribute<OneToMany> {

    public OneToManyRelationshipEntityAttribute(JpaPropertyInfo info, OneToMany oneToMany) {
        super(info, oneToMany);
    }

    @Override
    public CascadeType[] getCascadeTypes() {
        return getRelationshipAnnotation().cascade();
    }

}
