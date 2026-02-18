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

import de.leycm.linguae.label.LocalLabel;
import de.leycm.linguae.label.PreDefinedLabel;
import de.leycm.linguae.placeholder.Mappings;
import de.leycm.linguae.placeholder.PsPattern;
import lombok.NonNull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * <p>
 * Default implementation of {@link LinguaeProvider}. Serves as the entry
 * point for initializing and accessing the Linguae API.
 * </p>
 *
 * <p>
 * Can be used directly or extended for custom behavior.
 * </p>
 *
 * @author LeyCM
 * @since 1.0.1
 */
public abstract class AbstractLinguaeProvider implements LinguaeProvider {
    private final Map<String, Map<String, String>> translationCache = new ConcurrentHashMap<>();

    private final HashMap<String, Function<String, Label>> labels;
    private final Function<String, TextComponent> parser;
    private final PsPattern pattern;

    protected AbstractLinguaeProvider(final @NonNull HashMap<String, Function<String, Label>> labels,
                                      final @NonNull Function<String, TextComponent> parser,
                                      final @NonNull PsPattern pattern) {
        this.labels = labels;
        this.parser = parser;
        this.pattern = pattern;
    }

    @Override
    public @NonNull PsPattern getDefaultPlaceholderPattern() {
        return pattern;
    }

    @Override
    public @NonNull Label createTranslatableLabel(final @NonNull String key) {
        return new LocalLabel(this, new Mappings(), key);
    }

    @Override
    public @NonNull Label createPreDefinedLabel(final @NonNull String pre) {
        return new PreDefinedLabel(this, new Mappings(), pre);
    }

    // Format: <[key:"input"]>
    @Override
    public @NonNull Label parseLabel(final @NonNull String parsable) throws ParseException {

        if (!parsable.startsWith("<[") || !parsable.endsWith("]>"))
            throw new ParseException("Invalid label format: " + parsable, 0);

        String inner = parsable.substring(2, parsable.length() - 2);
        int colonIndex = inner.indexOf(":\"");
        int endQuoteIndex = inner.lastIndexOf("\"");

        if (colonIndex == -1 || endQuoteIndex == -1 || endQuoteIndex <= colonIndex + 2)
            throw new ParseException("Invalid label key/input structure: " + parsable, colonIndex);

        String key = inner.substring(0, colonIndex).trim();
        String input = inner.substring(colonIndex + 2, endQuoteIndex);

        String shortInput = input.length() > 23 ? input.substring(0, 20) + "..." : input;


        if (!labels.containsKey(key))
            throw new RuntimeException("Failed to parse: unknown key \"" + key + "\" from Label <[" + key + ':' + shortInput + "]>");

        return labels.get(key).apply(input);
    }

    @Override
    public @NonNull String translate(final @NonNull String key, final @NonNull Locale lang) {

        String languageKey = lang.toLanguageTag();

        translationCache.computeIfAbsent(languageKey, l -> {
            try {
                return new ConcurrentHashMap<>(loadLanguage(lang));
            } catch (Exception e) {
                throw new RuntimeException("Failed to load language " + lang, e);
            }
        });

        Map<String, String> langMap = translationCache.get(languageKey);

        if (langMap.containsKey(key))
            return langMap.get(key);

        String missing = handleMissingTranslation(key, lang);
        langMap.put(key, missing);

        return missing;
    }

    @Override
    public @NonNull TextComponent parseText(final @NonNull String text) {
        return parser.apply(text);
    }

    private @NonNull String missTranslation(final @NonNull String key,
                                            final @NonNull Locale lang) {
        if (lang.equals(Locale.ENGLISH)) return handleMissingTranslation(key, lang);

        return translate(key, lang);
    }

    protected abstract @NonNull Map<String, String> loadLanguage(final @NonNull Locale lang) throws Exception;

    protected abstract @NonNull String handleMissingTranslation(final @NonNull String key,
                                                                final @NonNull Locale lang);
}
