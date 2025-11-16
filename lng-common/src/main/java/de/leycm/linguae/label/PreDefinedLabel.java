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
package de.leycm.linguae.label;

import de.leycm.linguae.Label;
import de.leycm.linguae.LinguaeProvider;
import de.leycm.linguae.placeholder.Mappings;

import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public record PreDefinedLabel(@NonNull LinguaeProvider provider,
                              @NonNull Mappings mappings,
                              @NonNull String pre
) implements Label {
    @Override
    public @NonNull String in(@NonNull Locale lang) {
        return pre;
    }

    @Override
    public @NotNull String toString() {
        return "<[pre:\"" + pre + "\"";
    }
}
