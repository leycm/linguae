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

import de.leycm.linguae.placeholder.PsPattern;

import lombok.NonNull;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.jetbrains.annotations.Contract;

import java.text.ParseException;
import java.util.Locale;

/**
 * Core interface for the Linguae localization and templating system.
 *
 * <p>Provides a singleton instance via {@link #getInstance()} and defines a standard
 * initialization contract. Serves as the main entry point for creating
 * and managing localizable labels and text components.</p>
 *
 * <p>The provider supports multiple placeholder syntaxes, text parsing, and
 * integration with Adventure components. Implementations should be thread-safe
 * and properly initialized before use.</p>
 *
 * @author LeyCM
 * @since 1.0.1
 */
public interface LinguaeProvider extends de.leycm.neck.instance.Initializable {

    /**
     * Returns the singleton instance of the {@code LinguaeProvider}.
     *
     * <p>This method relies on the {@link de.leycm.neck.instance.Initializable#getInstance(Class)}
     * mechanism to retrieve the registered implementation.</p>
     *
     * <p>The provider must be initialized via {@link #onInstall()} before first use
     * to ensure proper configuration and resource loading.</p>
     *
     * @return the singleton instance of {@code LinguaeProvider}
     * @throws NullPointerException if no implementation is registered
     */
    @Contract(pure = true)
    static @NonNull LinguaeProvider getInstance() {
        return de.leycm.neck.instance.Initializable.getInstance(LinguaeProvider.class);
    }

    /**
     * Returns the default placeholder mapping rule used by this provider.
     *
     * <p>This rule is used when adding mappings without specifying an explicit rule,
     * providing a consistent default placeholder syntax across the application.</p>
     *
     * @return the default mapping rule, never null
     */
    @NonNull
    PsPattern getDefaultPlaceholderPattern();

    /**
     * Creates a new translatable label with the specified translation key.
     *
     * <p>Translatable labels resolve their content at runtime based on the current
     * locale and available translation resources. The actual translation lookup
     * is performed when {@link Label#in(Locale)} is called.</p>
     *
     * @param key the translation key used to look up localized text
     * @return a new translatable label instance, never null
     * @throws NullPointerException if key is null
     */
    @NonNull Label createTranslatableLabel(@NonNull String key);

    /**
     * Creates a new predefined label with the specified static text.
     *
     * <p>Predefined labels use the provided text as-is without translation lookup.
     * They still support placeholder mapping and can be converted to components.</p>
     *
     * @param pre the static text content for the label
     * @return a new predefined label instance, never null
     * @throws NullPointerException if pre is null
     */
    @NonNull Label createPreDefinedLabel(@NonNull String pre);

    /**
     * Parses a string representation into a label instance.
     *
     * <p>The parsing format is implementation-dependent but typically supports
     * both translatable and predefined label syntaxes. This allows for
     * flexible label creation from configuration files or user input.</p>
     *
     * @param parsable the string to parse into a label
     * @return the parsed label instance, never null
     * @throws ParseException if the string cannot be parsed as a valid label
     * @throws NullPointerException if parsable is null
     */
    @NonNull Label parseLabel(@NonNull String parsable)
            throws ParseException;

    /**
     * Translates a key to a string in the specified locale.
     *
     * @param key the translation key
     * @param lang the target locale
     * @return the translated string
     * @throws NullPointerException if key or lang is null
     */
    @NonNull String translate(@NonNull String key, @NonNull Locale lang);

    /**
     * Parses plain text into an Adventure Component.
     *
     * <p>The parsing may support additional formatting or markup syntax depending
     * on the implementation. This enables rich text rendering while maintaining
     * separation between content and presentation.</p>
     *
     * @param text the text to parse into a component
     * @return the parsed component, never null
     * @throws NullPointerException if text is null
     */
    @NonNull TextComponent parseText(@NonNull String text);
}