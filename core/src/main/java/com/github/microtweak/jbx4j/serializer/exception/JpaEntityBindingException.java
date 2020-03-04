package com.github.microtweak.jbx4j.serializer.exception;

/**
 * @author Marcos Koch Salvador
 * @since 1.0.0
 */
public class JpaEntityBindingException extends RuntimeException {

    private static final long serialVersionUID = -6990773797236630082L;

    public JpaEntityBindingException(String message) {
        super(message);
    }

    public JpaEntityBindingException(String message, Throwable cause) {
        super(message, cause);
    }

}
