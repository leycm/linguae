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
package de.leycm.linguae.placeholder;

import de.leycm.linguae.LinguaeProvider;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * A thread-safe, immutable container for placeholder mappings.
 *
 * <p>Facilitates string transformation by replacing placeholders with their
 * corresponding values. Supports multiple mapping rules and provides a fluent
 * API for building mappings.</p>
 *
 * <p>Instances are immutable - all modification operations return new instances.</p>
 *
 * @author LeyCM
 * @since 1.0.1
 */
public record Mappings(List<Mapping> mappings) {

    /**
     * Constructs an empty Mappings with no mappings.
     */
    public Mappings() {
        this(new ArrayList<>());
    }

    /**
     * Constructs a Mappings with the specified mappings.
     *
     * <p>The provided list is copied to ensure immutability.</p>
     *
     * @param mappings the initial mappings to include
     * @throws NullPointerException if mappings is null
     */
    public Mappings(final @NonNull List<Mapping> mappings) {
        this.mappings = new ArrayList<>(mappings);
    }

    /**
     * Adds a new mapping using the default placeholder rule from the default provider.
     *
     * <p>This is a convenience method that uses the singleton LinguaeProvider instance
     * and its default placeholder rule.</p>
     *
     * @param key the placeholder key to replace
     * @param value the value to substitute
     * @return a new Mappings instance with the added mapping
     * @throws NullPointerException if key or value is null
     */
    public @NonNull Mappings add(final @NonNull String key,
                                 final @NonNull Object value) {
        return add(LinguaeProvider.getInstance(), key, () -> value);
    }

    /**
     * Adds a new mapping using the default placeholder rule from the default provider.
     *
     * <p>This is a convenience method that uses the singleton LinguaeProvider instance
     * and its default placeholder rule.</p>
     *
     * @param key the placeholder key to replace
     * @param value the supplier providing the value to substitute
     * @return a new Mappings instance with the added mapping
     * @throws NullPointerException if key or value is null
     */
    public @NonNull Mappings add(final @NonNull String key,
                                 final @NonNull Supplier<Object> value) {
        return add(LinguaeProvider.getInstance(), key, value);
    }

    /**
     * Adds a new mapping using the default placeholder rule from the specified provider.
     *
     * @param provider the LinguaeProvider to get the default placeholder rule from
     * @param key the placeholder key to replace
     * @param value the supplier providing the value to substitute
     * @return a new Mappings instance with the added mapping
     * @throws NullPointerException if provider, key, or value is null
     */
    public @NonNull Mappings add(final @NonNull LinguaeProvider provider,
                                 final @NonNull String key,
                                 final @NonNull Supplier<Object> value) {
        return add(provider.getDefaultPlaceholderPattern(), key, value);
    }

    /**
     * Adds a new mapping with the specified rule, key, and value.
     *
     * <p>The value is converted to string using {@link String#valueOf(Object)}.
     * Returns a new Mappings instance, leaving the original unchanged.</p>
     *
     * @param rule the mapping rule to use for this placeholder
     * @param key the placeholder key to replace
     * @param value the supplier providing the value to substitute
     * @return a new Mappings instance with the added mapping
     * @throws NullPointerException if rule, key, or value is null
     */
    public @NonNull Mappings add(final @NonNull PsPattern rule,
                                 final @NonNull String key,
                                 final @NonNull Supplier<Object> value) {
        final Mappings newMapper = new Mappings(this.mappings);
        newMapper.mappings.add(new Mapping(rule, key, () -> String.valueOf(value.get())));
        return newMapper;
    }

    /**
     * Applies all mappings to the input text, replacing placeholders with their values.
     *
     * <p>Mappings are applied in the order they were added. If no mappings are present,
     * the original text is returned unchanged.</p>
     *
     * @param text the input text containing placeholders
     * @return the text with all placeholders replaced by their mapped values
     * @throws NullPointerException if text is null
     */
    public @NonNull String map(final @NonNull String text) {
        if (mappings.isEmpty()) return text;

        String result = text;

        for (final Mapping mapping : mappings)
            result = mapping.map(result);

        return result;
    }

    /**
     * Returns the number of mappings in this mapper.
     *
     * @return the number of mappings
     */
    public int size() {
        return mappings.size();
    }

    /**
     * Checks if this mapper contains no mappings.
     *
     * @return true if there are no mappings, false otherwise
     */
    public boolean isEmpty() {
        return mappings.isEmpty();
    }
}