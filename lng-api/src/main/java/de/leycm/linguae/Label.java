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
package de.leycm.linguae;

import de.leycm.linguae.mapping.Mapping;
import de.leycm.linguae.mapping.MappingRule;
import de.leycm.linguae.mapping.Mappings;

import lombok.NonNull;

import java.util.Locale;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Represents a localizable text element.
 *
 * <p>Labels can be either translatable (looked up from resource bundles)
 * or predefined (static text). Supports placeholder mapping and component conversion.</p>
 *
 * <p>Implementations should be immutable and thread-safe.</p>
 *
 * @since 1.0.1
 * @author Lennard [leycm@proton.me]
 */
public interface Label {

    /**
     * Creates a translatable label with the given key using the default provider.
     *
     * <p>Translatable labels are resolved at runtime based on the current locale
     * and available resource bundles.</p>
     *
     * @param key the translation key
     * @return a translatable label for the given key, never null
     * @throws NullPointerException if key is null
     */
    static @NonNull Label of(final @NonNull String key) {
        return of(LinguaeProvider.getInstance(), key);
    }

    /**
     * Creates a translatable label with the given key and a fallback using the default provider.
     *
     * <p>Translatable labels are resolved at runtime based on the current locale
     * and available resource bundles.</p>
     *
     * @param key the translation key
     * @param fallback the fallback text to use if translation is missing (optional)
     * @return a translatable label for the given key, never null
     * @throws NullPointerException if key or fallback is null
     */
    static @NonNull Label of(final @NonNull String key,
                             final @NonNull String fallback) {
        return of(LinguaeProvider.getInstance(), key, fallback);
    }

    /**
     * Creates a translatable label with the given key and a fallback function using the default provider.
     *
     * <p>Translatable labels are resolved at runtime based on the current locale
     * and available resource bundles. The fallback function generates locale-specific
     * fallback text when translation is missing.</p>
     *
     * @param key the translation key
     * @param fallback the fallback function to generate fallback text based on locale
     * @return a translatable label for the given key, never null
     * @throws NullPointerException if key or fallback is null
     */
    static @NonNull Label of(final @NonNull String key,
                             final @NonNull Function<Locale, String> fallback) {
        return of(LinguaeProvider.getInstance(), key, fallback);
    }

    /**
     * Creates a translatable label with the given key using the specified provider.
     *
     * @param provider the provider to create the label with
     * @param key the translation key
     * @return a translatable label for the given key, never null
     * @throws NullPointerException if factory or key is null
     */
    static @NonNull Label of(final @NonNull LinguaeProvider provider,
                                       final @NonNull String key) {
        return provider.createLabel(key, provider.createFallback(key));
    }

    /**
     * Creates a translatable label with the given key using the specified provider.
     *
     * @param provider the provider to create the label with
     * @param key the translation key
     * @param fallback the fallback text to use if translation is missing
     * @return a translatable label for the given key, never null
     * @throws NullPointerException if factory or key or fallback is null
     */
    static @NonNull Label of(final @NonNull LinguaeProvider provider,
                             final @NonNull String key,
                             final @NonNull String fallback) {
        return provider.createLabel(key, locale -> fallback);
    }

    /**
     * Creates a translatable label with the given key using the specified provider.
     *
     * @param provider the provider to create the label with
     * @param key the translation key
     * @param fallback the fallback function to generate fallback text based on locale
     * @return a translatable label for the given key, never null
     * @throws NullPointerException if factory or key or fallback is null
     */
    static @NonNull Label of(final @NonNull LinguaeProvider provider,
                             final @NonNull String key,
                             final @NonNull Function<Locale, String> fallback) {
        return provider.createLabel(key, fallback);
    }

    /**
     * Creates a predefined label with the given text using the default provider.
     *
     * <p>Predefined labels represent static text that doesn't require translation
     * lookup and is used as-is.</p>
     *
     * @param literal the predefined text content
     * @return a predefined label with the given text, never null
     * @throws NullPointerException if literal is null
     */
    static @NonNull Label literal(final @NonNull String literal) {
        return literal(LinguaeProvider.getInstance(), literal);
    }

    /**
     * Creates a predefined label with the given text using the specified provider.
     *
     * @param provider the provider to create the label with
     * @param literal the predefined text content
     * @return a predefined label with the given text, never null
     * @throws NullPointerException if factory or pre is null
     */
    static @NonNull Label literal(final @NonNull LinguaeProvider provider,
                                  final @NonNull String literal) {
        return provider.createLiteralLabel(literal);
    }

    /**
     * Returns the provider associated with this label.
     *
     * @return the {@link LinguaeProvider} that created this label, never null
     */
    @NonNull LinguaeProvider provider();

    /**
     * Returns the current mappings applied to this label.
     *
     * @return the mapper containing placeholder mappings, never null
     */
    @NonNull Mappings mappings();

    /**
     * Adds a new mapping with the specified key and value using the default placeholder rule.
     *
     * <p>Returns this Label instance with the added mapping.</p>
     *
     * @param key the placeholder key to replace
     * @param value the value to substitute for the placeholder
     * @return this Label instance with the added mapping, never null
     * @throws NullPointerException if key or value is null
     */
    default @NonNull Label withMapping(final @NonNull String key,
                                       final @NonNull Object value) {
        return withMapping(key, () -> value);
    }

    /**
     * Adds a new mapping with the specified key and value supplier using the default placeholder rule.
     *
     * <p>Returns this Label instance with the added mapping.</p>
     *
     * @param key the placeholder key to replace
     * @param supplier the supplier providing the value to substitute for the placeholder
     * @return this Label instance with the added mapping, never null
     * @throws NullPointerException if key or value is null
     */
    default @NonNull Label withMapping(final @NonNull String key,
                                       final @NonNull Supplier<Object> supplier) {
        return withMapping(provider().getMappingRule(), key, supplier);
    }

    /**
     * Adds a new mapping with the specified rule, key, and value supplier.
     *
     * <p>Returns this Label instance with the added mapping.</p>
     *
     * @param rule the mapping rule to use for this placeholder
     * @param key the placeholder key to replace
     * @param supplier the supplier providing the value to substitute for the placeholder
     * @return this Label instance with the added mapping, never null
     * @throws NullPointerException if rule, key, or value is null
     */
    default @NonNull Label withMapping(final @NonNull MappingRule rule,
                                       final @NonNull String key,
                                       final @NonNull Supplier<Object> supplier) {
        return withMapping(new Mapping(rule, key, () -> String.valueOf(supplier.get())));
    }

    /**
     * Adds a new mapping to this label.
     *
     * <p>Returns a new Label instance with the added mapping.</p>
     *
     * @param mapping the mapping to add
     * @return a new Label instance with the added mapping, never null
     * @throws NullPointerException if mapping is null
     */
    default @NonNull Label withMapping(final @NonNull Mapping mapping) {
        mappings().add(mapping);
        return this;
    }

    /**
     * Renders this label as a string in the specified default locale of the Provider.
     *
     * <p>For translatable labels, this performs translation lookup.
     * For literal Labels, this returns the static text.</p>
     *
     * @return the rendered string in the specified locale, never null
     * @throws NullPointerException if lang is null
     */
    default @NonNull String in() {
        return in(provider().getLocale());
    }

    /**
     * Renders this label as a string in the specified default locale of the Provider.
     *
     * <p>For translatable labels, this performs translation lookup.
     * For literal Labels, this returns the static text.</p>
     *
     * @return the rendered string in the specified locale, never null
     * @throws NullPointerException if lang is null
     */
    default @NonNull String mapped() {
        return mapped(provider().getLocale());
    }

    /**
     * Renders this label as a string in the specified locale.
     *
     * <p>For translatable labels, this performs translation lookup.
     * For literal Labels, this returns the static text.</p>
     *
     * @param locale the target locale for rendering
     * @return the rendered string in the specified locale, never null
     * @throws NullPointerException if lang is null
     */
    @NonNull String in(@NonNull Locale locale);

    /**
     * Renders this label with applied mappings as a string in the specified locale.
     *
     * <p>For translatable labels, this performs translation lookup and placeholder
     * replacement. For predefined labels, this returns the static text with
     * any applied mappings.</p>
     *
     * @param locale the target locale for rendering
     * @return the rendered string with placeholders replaced, never null
     * @throws NullPointerException if lang is null
     */
    default @NonNull String mapped(final @NonNull Locale locale) {
        return mappings().apply(in(locale));
    }

    /**
     * Renders this label formated out of the String as a {@code type} in the specified locale.
     *
     * <p>For translatable labels, this performs translation lookup.
     * For literal Labels, this returns the static text.</p>
     *
     * @param locale the target locale for rendering
     * @return the rendered string in the specified locale, never null
     * @throws NullPointerException if lang is null
     */
    default <T> @NonNull T in(@NonNull Locale locale, final @NonNull Class<T> type) {
        return provider().format(in(locale), type);
    }

    /**
     * Renders this label with applied mappings formated out of the String as a {@code type} in the specified locale.
     *
     * <p>For translatable labels, this performs translation lookup and placeholder
     * replacement. For predefined labels, this returns the static text with
     * any applied mappings.</p>
     *
     * @param locale the target locale for rendering
     * @return the rendered string with placeholders replaced, never null
     * @throws NullPointerException if lang is null
     */
    default <T> @NonNull T mapped(final @NonNull Locale locale, final @NonNull Class<T> type) {
        return provider().format(mappings().apply(in(locale)), type);
    }

    /**
     * Returns this label as a string using default locale settings.
     *
     * <p>The exact behavior is implementation-dependent but typically uses a
     * system default or fallback locale.</p>
     *
     * @return the label rendered as a string, never null
     */
    default @NonNull String asString() {
        return provider().serialize(this, String.class);
    }

    /**
     * Returns a string representation of this label.
     *
     * <p>The exact format is implementation-dependent but should provide meaningful
     * information about the label's type and content.</p>
     *
     * @return a string representation of this label, never null
     */
    @Override
    @NonNull String toString();

    /**
     * Compares this label to another object for equality.
     *
     * <p>Two labels are considered equal if they have the same provider, key, and
     * mappings. The exact equality criteria may vary based on the implementation.</p>
     *
     * @param obj the object to compare with
     * @return true if this label is equal to the other object, false otherwise
     */
    @Override
    boolean equals(Object obj);

    /**
     * Returns a hash code value for this label.
     *
     * <p>The hash code should be consistent with the equals method, meaning that
     * equal labels must have the same hash code.</p>
     *
     * @return a hash code value for this label
     */
    @Override
    int hashCode();

}