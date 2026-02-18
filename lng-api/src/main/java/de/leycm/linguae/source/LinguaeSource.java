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
package de.leycm.linguae.source;

import lombok.NonNull;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Represents a source of translations for different languages.
 *
 * <p>
 * A {@code TranslationSource} acts as an abstraction layer between the translation
 * system and the actual storage backend. Implementations may load translations
 * from various sources such as:
 * </p>
 *
 * <ul>
 *   <li>Local file systems (e.g. JSON, YAML, properties files)</li>
 *   <li>Databases</li>
 *   <li>Remote APIs / HTTP services</li>
 *   <li>Git repositories or CDNs</li>
 *   <li>In-memory providers</li>
 * </ul>
 *
 * <p>
 * This interface is designed to be implementation-agnostic and allows multiple
 * translation providers to coexist in a modular architecture (e.g. plugin systems,
 * microservices, distributed systems).
 * </p>
 *
 * @since 1.2.0
 * @author Lennard [leycm@proton.me]
 */
public interface LinguaeSource {

    /**
     * Returns all languages that are supported by this translation source.
     *
     * <p>
     * Implementations may return an empty list if the supported languages cannot be
     * determined (e.g. remote directory scanning is unavailable, disabled, or failed).
     * </p>
     *
     * @return an immutable or mutable list of supported {@link Locale Locales}, never {@code null}
     */
    @NonNull List<Locale> getSupportedLanguages();

    /**
     * Loads all translations for the given language.
     *
     * <p>
     * The returned map must contain translation keys as map keys and their resolved
     * localized values as map values.
     * </p>
     *
     * <p>
     * Implementations may load translations from local files, databases, remote APIs,
     * or other external sources.
     * </p>
     *
     * @param locale the {@link Locale} to load translations for, must not be {@code null}
     * @return a map of translation keys to localized strings, never {@code null}
     * @throws Exception if loading fails due to I/O errors, parsing errors,
     *                   connection issues, or invalid data formats
     */
    @NonNull Map<String, String> loadLanguage(@NonNull Locale locale) throws Exception;


    /**
     * Checks whether the given language is supported by this translation source.
     *
     * <p>
     * This method should return {@code true} if the source can provide translations
     * for the given locale, either directly or via fallback/alias mechanisms
     * (e.g. {@code de_DE} → {@code de}).
     * </p>
     *
     * @param locale the {@link Locale} to check, must not be {@code null}
     * @return {@code true} if the language is supported, otherwise {@code false}
     */
    boolean supportsLanguage(@NonNull Locale locale);

}
