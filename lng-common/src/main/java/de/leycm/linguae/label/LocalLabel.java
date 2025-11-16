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

import java.util.Locale;

public record LocalLabel(@NonNull LinguaeProvider provider,
                         @NonNull Mappings mappings,
                         @NonNull String key
) implements Label {

    @Override
    public @NonNull String in(final @NonNull Locale lang) {
        return provider.translate(key, lang);
    }

    @Override
    public @NonNull String toString() {
        return "<[loc:\"" + key +"\"]>";
    }

}
