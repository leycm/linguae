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

import de.leycm.linguae.exeption.FormatException;
import de.leycm.linguae.mapping.MappingRule;

import de.leycm.neck.instance.Initializable;
import lombok.NonNull;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Contract;

import java.text.ParseException;
import java.util.Locale;
import java.util.function.Function;

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
public interface LinguaeProvider extends Initializable {

    /**
     * Returns the singleton instance of the {@code LinguaeProvider}.
     *
     * <p>This method relies on the {@link Initializable#getInstance(Class)}<br>
     * mechanism to retrieve the registered implementation.</p>
     *
     * <p>The provider must be initialized via {@link #onInstall()} before first use<br>
     * to ensure proper configuration and resource loading.</p>
     *
     * @return the singleton instance of {@code LinguaeProvider}
     * @throws NullPointerException if no implementation is registered
     */
    @Contract(pure = true)
    static @NonNull LinguaeProvider getInstance() {
        return Initializable.getInstance(LinguaeProvider.class);
    }

    /**
     * Returns the placeholder {@link MappingRule} used by this provider.
     *
     * <p>This rule is used when adding mappings without specifying an explicit rule,<br>
     * providing a consistent default placeholder syntax across the provider.</p>
     *
     * @return the default mapping rule, never null
     */
    @NonNull
    MappingRule getMappingRule();

    /**
     * Parses a string representation into a label instance.
     *
     * <p>The parsing format is implementation-dependent but typically<br>
     * supports both translatable and predefined label syntaxes.<br>
     * This allows for flexible label creation from configuration files or user input.</p>
     *
     * @deprecated since 1.2.0. Use the {@link #deserialize(Object)} method instead.
     * @param parsable the string to parse into a label
     * @return the parsed label instance, never null
     * @throws ParseException if the string cannot be parsed as a valid label
     * @throws NullPointerException if parsable is null
     */
    @Deprecated(since = "1.2.0")
    default @NonNull Label createFromString(final @NonNull String parsable)
            throws ParseException {
        return deserialize(parsable);
    }

    default @NonNull Function<Locale, String> createFallback(final @NonNull String key) {
        return locale -> "[" + locale.toLanguageTag().toLowerCase() + "." + key + "]";
    }

    /**
     * Creates a new translatable label with the specified translation key.
     *
     * <p>Translatable labels resolve their content at runtime based on the current<br>
     * locale and available translation resources. The actual translation lookup<br>
     * is performed when {@link Label#in(Locale)} is called.</p>
     *
     * @param key the translation key used to look up localized text
     * @param fallback the fallback function to generate text when translation is missing
     * @return a new translatable label instance, never null
     * @throws NullPointerException if key or fallback is null
     */
    @NonNull Label createLabel(@NonNull String key,
                               @NonNull Function<Locale, String> fallback);

    /**
     * Creates a new predefined label with the specified static text.
     *
     * <p>Literal labels use the provided text as-is without translation lookup.<br>
     * They still support placeholder mapping and can be converted to components.</p>
     *
     * @param literal the static text content for the label
     * @return a new predefined label instance, never null
     * @throws NullPointerException if literal is null
     */
    @NonNull Label createLiteralLabel(@NonNull String literal);

    /**
     * Translates a key to a string in the specified locale.
     *
     * @param key the translation key
     * @param lang the target locale
     * @return the translated string
     * @throws NullPointerException if key or lang is null
     */
    @NonNull String translate(@NonNull String key,
                              @NonNull Locale lang);

    /**
     * Serializes a Label into another extern type.
     *
     * <p>The serialization format is implementation-dependent and may <br>
     * vary based on the target type and implementation of this type.</p>
     *
     * @param label the label to serialize into another type
     * @param type the target type of the serialized Object
     * @throws NullPointerException if serialized is null
     */
    @NonNull <T> T serialize(@NonNull Label label,
                             @NonNull Class<T> type);

    /**
     * Deserializes a before serialized Object back into a Label instance.
     *
     * <p>The deserialize format is implementation-dependent and may <br>
     * vary based on the target type and implementation of this type.</p>
     *
     * @param serialized the serialized Object to deserialize into a Label
     * @throws ParseException if the serialized Object cannot be parsed into a Label
     * @throws NullPointerException if serialized is null
     */
    @NonNull Label deserialize(@NonNull Object serialized)
            throws ParseException;

    /**
     * Formats a raw string into a serialized external representation.
     *
     * <p>This method behaves similar to {@link #serialize(Label, Class)}, but<br>
     * takes a raw string as input instead of a Label. It's used to provide<br>
     * translations in any format, like {@link Component} and more.</p>
     *
     * @param input the raw string input to format
     * @param type the target type of the formatted representation
     * @return the formatted serialized representation
     * @throws NullPointerException if input or type is null
     * @throws ParseException if the input cannot be parsed/formatted
     */
    @NonNull <T> T format(@NonNull String input,
                          @NonNull Class<T> type
    ) throws FormatException;
}