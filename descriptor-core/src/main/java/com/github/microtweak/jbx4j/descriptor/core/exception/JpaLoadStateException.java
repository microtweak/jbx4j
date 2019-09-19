package com.github.microtweak.jbx4j.descriptor.core.exception;

/**
 * @author Marcos Koch Salvador
 * @since 1.0.0
 */
public class JpaLoadStateException extends RuntimeException {

    public JpaLoadStateException(String message) {
        super(message);
    }

    public JpaLoadStateException(String message, Throwable cause) {
        super(message, cause);
    }

    public JpaLoadStateException(Throwable cause) {
        super(cause);
    }

}