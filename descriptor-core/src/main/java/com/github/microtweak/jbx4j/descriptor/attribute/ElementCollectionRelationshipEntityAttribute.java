package com.github.microtweak.jbx4j.descriptor.attribute;

import com.github.microtweak.jbx4j.descriptor.JpaPropertyInfo;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;

/**
 * Concrete implementation of {@link RelationshipEntityAttribute} for {@link ElementCollection} relationship
 *
 * @author Marcos Koch Salvador
 * @since 1.0.0
 *
 * @see RelationshipEntityAttribute
 * @see EntityAttribute
 * @see ElementCollection
 */
public class ElementCollectionRelationshipEntityAttribute extends RelationshipEntityAttribute<ElementCollection> {

    private CascadeType[] cascadeType;

    public ElementCollectionRelationshipEntityAttribute(JpaPropertyInfo info, ElementCollection elementCollection) {
        super(info, elementCollection);
        cascadeType = new CascadeType[0];
    }

    @Override
    public CascadeType[] getCascadeTypes() {
        return cascadeType;
    }

}
