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
package de.leycm.linguae.serialize;

import de.leycm.linguae.Label;
import de.leycm.linguae.exeption.FormatException;
import lombok.NonNull;

import java.text.ParseException;

public interface LabelSerializer<T> {

    /**
     * Serializes a {@link Label} into an external representation.
     *
     * <p>The resulting format is implementation-dependent and may vary
     * depending on the concrete serializer implementation.</p>
     *
     * @param label the Label to serialize
     * @return the external serialized representation
     * @throws NullPointerException if label is null
     */
    @NonNull T serialize(@NonNull Label label);

    /**
     * Deserializes an external representation back into a {@link Label}.
     *
     * @param serialized the external representation to deserialize
     * @return the deserialized Label instance
     * @throws ParseException if the serialized representation cannot be parsed into a Label
     * @throws NullPointerException if serialized is null
     */
    @NonNull Label deserialize(@NonNull T serialized)
            throws ParseException;

    /**
     * Formats a translated raw string into an external serialized representation.
     *
     * <p>This method behaves similar to {@link #serialize(Label)}, but
     * takes a raw translated string as input instead of a Label instance.</p>
     *
     * @param translated the translated/raw string to format
     * @return the formatted external representation
     * @throws NullPointerException if translated is null
     */
    @NonNull T format(@NonNull String translated)
            throws FormatException;
}
