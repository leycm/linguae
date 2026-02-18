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
package de.leycm.linguae.mapping;

import lombok.NonNull;

import java.util.function.Supplier;
import java.util.regex.Matcher;

/**
 * Represents a single placeholder mapping rule.
 *
 * <p>This record defines a pattern to identify placeholders, the key to match,
 * and the value supplier for replacement.</p>
 *
 * @param rule the pattern used to identify placeholders
 * @param key the placeholder key to match
 * @param value the supplier providing the replacement value
 *
 * @since 1.0.1
 * @author Lennard [leycm@proton.me]
 */
public record Mapping(@NonNull MappingRule rule,
                      @NonNull String key,
                      @NonNull Supplier<String> value) {

    /**
     * Constructs a new Mapping with the specified rule, key, and value supplier.
     *
     * @param rule the pattern used to identify placeholders
     * @param key the placeholder key to match
     * @param value the supplier providing the replacement value
     */
    public Mapping {
    }

    /**
     * Applies this mapping to the input text, replacing matched placeholders.
     *
     * @param text the input text to process
     * @return the text with placeholders replaced by their mapped values, never null
     * @throws NullPointerException if text is null
     */
    public @NonNull String map(final @NonNull String text) {
        final Matcher matcher = rule.getPattern().matcher(text);
        final StringBuilder result = new StringBuilder(text.length());

        int lastEnd = 0;
        while (matcher.find()) {
            if (!matcher.group(1).equals(key)) continue;
            result.append(text, lastEnd, matcher.start());
            result.append(value.get());
            lastEnd = matcher.end();
        }

        if (lastEnd == 0) return text;

        result.append(text, lastEnd, text.length());
        return result.toString();
    }
}