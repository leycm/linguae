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
 * Represents a localizable text element.
 *
 * <p>Labels can be either translatable (looked up from resource bundles)
 * or predefined (static text). Supports placeholder mapping and component conversion.</p>
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
     * <p>This method delegates to the singleton LinguaeProvider instance to parse
     * the label according to the provider's parsing rules.</p>
     *
     * @param parsable the string to parse into a label
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
     * @param factory the provider to use for parsing
     * @param parsable the string to parse into a label
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
     * <p>Translatable labels are resolved at runtime based on the current locale
     * and available resource bundles.</p>
     *
     * @param key the translation key
     * @return a translatable label for the given key, never null
     * @throws NullPointerException if key is null
     */
    static @NonNull Label translatable(final @NonNull String key) {
        return translatable(LinguaeProvider.getInstance(), key);
    }

    /**
     * Creates a translatable label with the given key using the specified provider.
     *
     * @param factory the provider to create the label with
     * @param key the translation key
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
     * <p>Predefined labels represent static text that doesn't require translation
     * lookup and is used as-is.</p>
     *
     * @param pre the predefined text content
     * @return a predefined label with the given text, never null
     * @throws NullPointerException if pre is null
     */
    static @NonNull Label predefined(final @NonNull String pre) {
        return predefined(LinguaeProvider.getInstance(), pre);
    }

    /**
     * Creates a predefined label with the given text using the specified provider.
     *
     * @param factory the provider to create the label with
     * @param pre the predefined text content
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
     * <p>For translatable labels, this performs translation lookup and placeholder
     * replacement. For predefined labels, this returns the static text with
     * any applied mappings.</p>
     *
     * @param lang the target locale for rendering
     * @return the rendered string in the specified locale, never null
     * @throws NullPointerException if lang is null
     */
    @NonNull String in(@NonNull Locale lang);

    /**
     * Returns a string representation of this label.
     *
     * <p>The exact format is implementation-dependent but should provide meaningful
     * information about the label's type and content.</p>
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

    /**
     * Renders this label with applied mappings in the specified locale.
     *
     * @param lang the target locale for rendering
     * @return the rendered string with placeholders replaced
     * @throws NullPointerException if lang is null
     */
    default @NonNull String mapped(final @NonNull Locale lang) {
        return mappings().map(in(lang));
    }

    /**
     * Renders this label as an Adventure Component in the specified locale.
     *
     * <p>This is a convenience method that delegates to the provider's text parsing
     * capabilities to convert the rendered string into a component.</p>
     *
     * @param lang the target locale for rendering
     * @return the rendered component in the specified locale, never null
     * @throws NullPointerException if lang is null
     */
    default @NonNull Component asComponent(final @NonNull Locale lang) {
        return provider().parseText(in(lang));
    }

    /**
     * Renders this label with applied mappings as an Adventure Component.
     *
     * @param lang the target locale for rendering
     * @return the rendered component with placeholders replaced
     * @throws NullPointerException if lang is null
     */
    default @NonNull Component asMappedComponent(final @NonNull Locale lang) {
        return provider().parseText(mappings().map(in(lang)));
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
        return toString();
    }
}