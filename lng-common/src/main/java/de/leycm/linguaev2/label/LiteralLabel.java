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
package de.leycm.linguaev2.label;

import de.leycm.linguae.Label;
import de.leycm.linguae.LinguaeProvider;
import de.leycm.linguae.mapping.Mappings;
import lombok.NonNull;

import java.util.Locale;

public record LiteralLabel(
        @NonNull LinguaeProvider provider,
        @NonNull Mappings mappings,
        @NonNull String literal
) implements Label {

    public LiteralLabel { }

    public LiteralLabel(@NonNull LinguaeProvider provider,
                        @NonNull String literal) {
        this(provider, new Mappings(provider), literal);
    }

    @Override
    public @NonNull String in(@NonNull Locale locale) {
        return literal;
    }

    @Override
    public @NonNull String toString() {
        return provider().serialize(this, String.class);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        LiteralLabel that = (LiteralLabel) obj;
        return literal.equals(that.literal);
    }

    @Override
    public int hashCode() {
        return literal.hashCode();
    }

}
