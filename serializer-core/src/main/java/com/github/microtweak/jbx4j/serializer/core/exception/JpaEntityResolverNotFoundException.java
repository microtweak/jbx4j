package com.github.microtweak.jbx4j.serializer.core.exception;

/**
 * @author Marcos Koch Salvador
 * @since 1.0.0
 */
public class JpaEntityResolverNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 4136866582917847442L;

    public JpaEntityResolverNotFoundException(String msg) {
        super(msg);
    }

}
