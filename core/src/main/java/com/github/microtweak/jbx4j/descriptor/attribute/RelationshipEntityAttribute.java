package com.github.microtweak.jbx4j.descriptor.attribute;

import com.github.microtweak.jbx4j.descriptor.JpaPropertyInfo;

import javax.persistence.CascadeType;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

/**
 * Abstract implementation for relationship attributes (OneToOne, ManyToOne, ManyToMany, OneToMany) and ElementCollection.
 *
 * Common operations declared in EntityAttribute are delegated to an instance of {@link CommonEntityAttribute}.
 *
 * @author Marcos Koch Salvador
 * @since 1.0.0
 *
 * @see EntityAttribute
 */
public abstract class RelationshipEntityAttribute<R extends Annotation> extends EntityAttribute {

    private R relationshipAnnotation;

    protected RelationshipEntityAttribute(JpaPropertyInfo info, R relationshipAnnotation) {
        super(info);
        this.relationshipAnnotation = relationshipAnnotation;
    }

    protected R getRelationshipAnnotation() {
        return relationshipAnnotation;
    }

    public abstract CascadeType[] getCascadeTypes();

    public boolean hasAnyCascade(CascadeType... cascadeTypes) {
        final List<CascadeType> ct = Arrays.asList(cascadeTypes);

        for (CascadeType cascade : getCascadeTypes()) {
            if (ct.contains(cascade)) {
                return true;
            }
        }

        return false;
    }

}
