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

import lombok.NonNull;

public class IncompatibleMatchException extends IllegalArgumentException {

    public IncompatibleMatchException(final @NonNull Class<?> type, Throwable cause) {
        super("Serializer for type " + type.getName() + " returned incompatible type", cause);
    }

}
