/**
 * LECP-LICENSE NOTICE
 * <br><br>
 * This Sourcecode is under the LECP-LICENSE. <br>
 * License at: <a href="https://github.com/leycm/leycm/blob/main/LICENSE">GITHUB</a>
 * <br><br>
 * Copyright (c) LeyCM <a href="mailto:leycm@proton.me">leycm@proton.me</a> l <br>
 * Copyright (c) maintainers <br>
 * Copyright (c) contributors
 */
package de.leycm.linguae.exeption;

/**
 * Exception thrown when a formatting or serialization process fails.
 *
 * <p>This exception indicates that a given input (e.g. String, Label, or
 * external representation) could not be formatted or converted into the
 * expected external format.</p>
 *
 * <p>Typical use cases include:</p>
 * <ul>
 *   <li>Invalid input syntax</li>
 *   <li>Unsupported format types</li>
 *   <li>Malformed translation strings</li>
 *   <li>Serialization/formatting pipeline errors</li>
 * </ul>
 *
 * <p>This is a {@link RuntimeException}, meaning it represents a
 * non-recoverable formatting error in most application flows.</p>
 *
 * <p>It is commonly used in serializer/formatter implementations
 * where formatting failures are considered programmer or data errors.</p>
 *
 * @since 1.2.0
 * @author Lennard [leycm@proton.me]
 */
public class FormatException extends RuntimeException {

    /**
     * Constructs a new {@code FormatException} with no detail message.
     */
    public FormatException() {
        super();
    }

    /**
     * Constructs a new {@code FormatException} with the specified detail message.
     *
     * @param message the detail message
     */
    public FormatException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code FormatException} with the specified detail message
     * and cause.
     *
     * @param message the detail message
     * @param cause the cause of this exception
     */
    public FormatException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new {@code FormatException} with the specified cause.
     *
     * @param cause the cause of this exception
     */
    public FormatException(Throwable cause) {
        super(cause);
    }

    /**
     * Advanced constructor allowing suppression and stack trace writability control.
     *
     * @param message the detail message
     * @param cause the cause
     * @param enableSuppression whether suppression is enabled
     * @param writableStackTrace whether the stack trace should be writable
     */
    protected FormatException(String message, Throwable cause,
                              boolean enableSuppression,
                              boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
