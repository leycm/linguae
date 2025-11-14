package de.leycm.linguae.placeholder;

import de.leycm.linguae.LinguaeProvider;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper
 *
 * <p>
 * A thread-safe, immutable container for placeholder mappings that facilitates
 * string transformation by replacing placeholders with their corresponding values.
 * Supports multiple mapping rules and provides a fluent API for building mappings.
 * </p>
 *
 * <p>Instances are immutable - all modification operations return new instances.</p>
 *
 * @param mappings the list of mappings managed by this mapper
 * @author LeyCM
 * @since 1.0.1
 */
public record Mapper(List<Mapping> mappings) {

    /**
     * Constructs an empty Mapper with no mappings.
     */
    public Mapper() {
        this(new ArrayList<>());
    }

    /**
     * Constructs a Mapper with the specified mappings.
     *
     * <p>
     * The provided list is copied to ensure immutability of the mapper instance.
     * </p>
     *
     * @param mappings the initial mappings to include, cannot be null
     * @throws NullPointerException if mappings is null
     */
    public Mapper(final @NonNull List<Mapping> mappings) {
        this.mappings = new ArrayList<>(mappings);
    }

    /**
     * Adds a new mapping using the default placeholder rule from the default provider.
     *
     * <p>
     * This is a convenience method that uses the singleton LinguaeProvider instance
     * and its default placeholder rule.
     * </p>
     *
     * @param key the placeholder key to replace, cannot be null
     * @param value the value to substitute, cannot be null
     * @return a new Mapper instance with the added mapping
     * @throws NullPointerException if key or value is null
     */
    public @NonNull Mapper add(final @NonNull String key,
                               final @NonNull Object value) {
        return add(LinguaeProvider.getInstance(), key, value);
    }

    /**
     * Adds a new mapping using the default placeholder rule from the specified provider.
     *
     * @param provider the LinguaeProvider to get the default placeholder rule from, cannot be null
     * @param key the placeholder key to replace, cannot be null
     * @param value the value to substitute, cannot be null
     * @return a new Mapper instance with the added mapping
     * @throws NullPointerException if provider, key, or value is null
     */
    public @NonNull Mapper add(final @NonNull LinguaeProvider provider,
                               final @NonNull String key,
                               final @NonNull Object value) {
        return add(provider.getDefaultPlaceholder(), key, value);
    }

    /**
     * Adds a new mapping with the specified rule, key, and value.
     *
     * <p>
     * The value is converted to string using {@link String#valueOf(Object)}.
     * Returns a new Mapper instance, leaving the original unchanged.
     * </p>
     *
     * @param rule the mapping rule to use for this placeholder, cannot be null
     * @param key the placeholder key to replace, cannot be null
     * @param value the value to substitute, cannot be null
     * @return a new Mapper instance with the added mapping
     * @throws NullPointerException if rule, key, or value is null
     */
    public @NonNull Mapper add(final @NonNull MappingRule rule,
                               final @NonNull String key,
                               final @NonNull Object value) {
        final Mapper newMapper = new Mapper(this.mappings);
        newMapper.mappings.add(new Mapping(rule, key, String.valueOf(value)));
        return newMapper;
    }

    /**
     * Applies all mappings to the input text, replacing placeholders with their values.
     *
     * <p>
     * Mappings are applied in the order they were added. If no mappings are present,
     * the original text is returned unchanged.
     * </p>
     *
     * @param text the input text containing placeholders, cannot be null
     * @return the text with all placeholders replaced by their mapped values
     * @throws NullPointerException if text is null
     */
    public @NonNull String map(final @NonNull String text) {
        if (mappings.isEmpty()) {
            return text;
        }

        String result = text;

        for (final Mapping mapping : mappings) {
            result = mapping.map(result);
        }

        return result;
    }

    /**
     * Checks if the text contains placeholders for the specified rule.
     *
     * @param text the text to check, cannot be null
     * @param rule the mapping rule to check for, cannot be null
     * @return true if the text contains the prefix for the given rule
     * @throws NullPointerException if text or rule is null
     */
    private boolean containsPlaceholder(final @NonNull String text,
                                        final @NonNull MappingRule rule) {
        return text.contains(rule.getPrefix());
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