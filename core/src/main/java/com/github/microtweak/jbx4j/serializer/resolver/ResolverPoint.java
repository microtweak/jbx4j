package com.github.microtweak.jbx4j.serializer.resolver;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;

@Getter
@EqualsAndHashCode
class ResolverPoint {

    private Class<?> parentType;
    private Class<?> rawType;
    private List<Annotation> annotations;

    ResolverPoint(Object parent, Class<?> rawType, List<Annotation> annotations) {
        this.parentType = Optional.ofNullable(parent).map(Object::getClass).orElse( null );
        this.rawType = rawType;
        this.annotations = annotations;
    }
}
