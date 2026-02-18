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
import de.leycm.linguae.mapping.Mappings;
import lombok.NonNull;

import java.util.Locale;
import java.util.function.Function;


public record LocaleLabel(
        @NonNull LinguaeProvider provider,
        @NonNull Mappings mappings,
        @NonNull String key,
        @NonNull Function<Locale, String> fallback
        ) implements Label {

    public LocaleLabel { }

    public LocaleLabel(@NonNull LinguaeProvider provider, @NonNull String key,
                       @NonNull Function<Locale, String> fallback) {
        this(provider, new Mappings(provider), key, fallback);
    }

    @Override
    public @NonNull String in(@NonNull Locale locale) {
        return provider().translate(key(), fallback, locale);
    }

    @Override
    public @NonNull String toString() {
        return provider().serialize(this, String.class);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        LocaleLabel that = (LocaleLabel) obj;
        return key().equals(that.key());
    }

    @Override
    public int hashCode() {
        return key().hashCode();
    }

}
