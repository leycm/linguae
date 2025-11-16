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

import de.leycm.linguae.placeholder.Mappings;

import lombok.NonNull;

import net.kyori.adventure.text.Component;

import java.text.ParseException;
import java.util.Locale;

/**
 * Label
 *
 * <p>
 * Represents a localizable text element that can be rendered in different languages
 * and formats. Labels can be either translatable (looked up from resource bundles)
 * or predefined (static text). Supports placeholder mapping and component conversion.
 * </p>
 *
 * <p>Implementations should be immutable and thread-safe.</p>
 *
 * @author LeyCM
 * @since 1.0.1
 */
public interface Label {

    /**
     * Parses a label from the given string using the default provider.
     *
     * <p>
     * This method delegates to the singleton LinguaeProvider instance to parse
     * the label according to the provider's parsing rules.
     * </p>
     *
     * @param parsable the string to parse into a label, cannot be null
     * @return the parsed label, never null
     * @throws ParseException if the string cannot be parsed as a valid label
     * @throws NullPointerException if parsable is null
     */
    static @NonNull Label parse(final @NonNull String parsable)
            throws ParseException {
        return parse(LinguaeProvider.getInstance(), parsable);
    }

    /**
     * Parses a label from the given string using the specified provider.
     *
     * @param factory the provider to use for parsing, cannot be null
     * @param parsable the string to parse into a label, cannot be null
     * @return the parsed label, never null
     * @throws ParseException if the string cannot be parsed as a valid label
     * @throws NullPointerException if factory or parsable is null
     */
    static @NonNull Label parse(final @NonNull LinguaeProvider factory,
                                final @NonNull String parsable)
            throws ParseException {
        return factory.parseLabel(parsable);
    }

    /**
     * Creates a translatable label with the given key using the default provider.
     *
     * <p>
     * Translatable labels are resolved at runtime based on the current locale
     * and available resource bundles.
     * </p>
     *
     * @param key the translation key, cannot be null
     * @return a translatable label for the given key, never null
     * @throws NullPointerException if key is null
     */
    static @NonNull Label translatable(final @NonNull String key) {
        return translatable(LinguaeProvider.getInstance(), key);
    }

    /**
     * Creates a translatable label with the given key using the specified provider.
     *
     * @param factory the provider to create the label with, cannot be null
     * @param key the translation key, cannot be null
     * @return a translatable label for the given key, never null
     * @throws NullPointerException if factory or key is null
     */
    static @NonNull Label translatable(final @NonNull LinguaeProvider factory,
                                       final @NonNull String key) {
        return factory.createTranslatableLabel(key);
    }

    /**
     * Creates a predefined label with the given text using the default provider.
     *
     * <p>
     * Predefined labels represent static text that doesn't require translation
     * lookup and is used as-is.
     * </p>
     *
     * @param pre the predefined text content, cannot be null
     * @return a predefined label with the given text, never null
     * @throws NullPointerException if pre is null
     */
    static @NonNull Label predefined(final @NonNull String pre) {
        return predefined(LinguaeProvider.getInstance(), pre);
    }

    /**
     * Creates a predefined label with the given text using the specified provider.
     *
     * @param factory the provider to create the label with, cannot be null
     * @param pre the predefined text content, cannot be null
     * @return a predefined label with the given text, never null
     * @throws NullPointerException if factory or pre is null
     */
    static @NonNull Label predefined(final @NonNull LinguaeProvider factory,
                                     final @NonNull String pre) {
        return factory.createPreDefinedLabel(pre);
    }

    /**
     * Renders this label as a string in the specified locale.
     *
     * <p>
     * For translatable labels, this performs translation lookup and placeholder
     * replacement. For predefined labels, this returns the static text with
     * any applied mappings.
     * </p>
     *
     * @param lang the target locale for rendering, cannot be null
     * @return the rendered string in the specified locale, never null
     * @throws NullPointerException if lang is null
     */
    @NonNull String in(final @NonNull Locale lang);

    /**
     * Returns a string representation of this label.
     *
     * <p>
     * The exact format is implementation-dependent but should provide meaningful
     * information about the label's type and content.
     * </p>
     *
     * @return a string representation of this label, never null
     */
    @NonNull String toString();

    /**
     * Returns the provider associated with this label.
     *
     * @return the LinguaeProvider that created this label, never null
     */
    @NonNull LinguaeProvider provider();

    /**
     * Returns the current mappings applied to this label.
     *
     * @return the mapper containing placeholder mappings, never null
     */
    @NonNull Mappings mappings();

    default @NonNull String mapped(final @NonNull Locale lang) {
        return mappings().map(in(lang));
    }

    /**
     * Renders this label as an Adventure Component in the specified locale.
     *
     * <p>
     * This is a convenience method that delegates to the provider's text parsing
     * capabilities to convert the rendered string into a component.
     * </p>
     *
     * @param lang the target locale for rendering, cannot be null
     * @return the rendered component in the specified locale, never null
     * @throws NullPointerException if lang is null
     */
    default @NonNull Component asComponent(final @NonNull Locale lang) {
        return provider().parseText(in(lang));
    }

    default @NonNull Component asMappedComponent(final @NonNull Locale lang) {
        return provider().parseText(mappings().map(in(lang)));
    }

    /**
     * Returns this label as a string using default locale settings.
     *
     * <p>
     * The exact behavior is implementation-dependent but typically uses a
     * system default or fallback locale.
     * </p>
     *
     * @return the label rendered as a string, never null
     */
    default @NonNull String asString() {
        return toString();
    }
}