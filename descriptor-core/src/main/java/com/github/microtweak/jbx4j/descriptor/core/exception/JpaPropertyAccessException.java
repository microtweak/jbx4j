package com.github.microtweak.jbx4j.descriptor.core.exception;

/**
 * @author Marcos Koch Salvador
 * @since 1.0.0
 */
public class JpaPropertyAccessException extends RuntimeException {

    private static final long serialVersionUID = -9074605014280434418L;

    public JpaPropertyAccessException(String msg) {
        super(msg);
    }

    public JpaPropertyAccessException(String msg, Throwable rootCause) {
        super(msg, rootCause);
    }

}