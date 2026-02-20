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
import de.leycm.linguae.serialize.LabelSerializer;
import de.leycm.linguae.source.LinguaeSource;
import de.leycm.linguae.exeption.IncompatibleMatchException;
import de.leycm.linguae.label.LiteralLabel;
import de.leycm.linguae.label.LocaleLabel;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;

import java.text.ParseException;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Slf4j
public class CommonLinguaeProvider implements LinguaeProvider {

    @Contract(value = " -> new", pure = true)
    public static @NonNull Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Map<Class<?>, LabelSerializer<?>> serializerRegistry;
        private MappingRule mappingRule;

        private Builder() {
            this.serializerRegistry = new ConcurrentHashMap<>();
            this.mappingRule = MappingRule.FSTRING;
        }

        public Builder withSerializer(final @NonNull Class<?> type,
                                      final @NonNull LabelSerializer<?> serializer) {
            this.serializerRegistry.put(type, serializer);
            return this;
        }

        public Builder mappingRule(final @NonNull MappingRule mappingRule) {
            this.mappingRule = mappingRule;
            return this;
        }

        public CommonLinguaeProvider build(final @NonNull LinguaeSource source) {
            return new CommonLinguaeProvider(serializerRegistry, mappingRule, source);
        }
    }

    private final Map<String, Map<String, String>> translationCache = new ConcurrentHashMap<>();
    private final Map<Class<?>, LabelSerializer<?>> serializerRegistry = new ConcurrentHashMap<>();
    private final MappingRule mappingRule;
    private final LinguaeSource source;


    private CommonLinguaeProvider(
            final @NonNull Map<Class<?>, LabelSerializer<?>> serializers,
            final @NonNull MappingRule mappingRule,
            final @NonNull LinguaeSource source) {
        this.mappingRule = mappingRule;
        this.serializerRegistry.putAll(serializers);
        this.source = source;
    }

    @Override
    public @NonNull LinguaeSource getSource() {
        return source;
    }

    @Override
    public @NonNull MappingRule getMappingRule() {
        return mappingRule;
    }

    @Override
    public @NonNull Label createLabel(@NonNull String key, @NonNull Function<Locale, String> fallback) {
        return new LocaleLabel(this, key, fallback);
    }

    @Override
    public @NonNull Label createLiteralLabel(@NonNull String literal) {
        return new LiteralLabel(this, literal);
    }

    @Override
    public @NonNull String translate(final @NonNull String key,
                                     final @NonNull Function<Locale, String> fallback,
                                     final @NonNull Locale locale) {
        return translationCache.computeIfAbsent(locale.toLanguageTag(), l -> {
                    Map<String, String> translations = null;
                    try {translations = source.loadLanguage(locale);
                    } catch (Exception e) {
                        // todo: maybe log here? but we clearly 
                        translations = new ConcurrentHashMap<>();
                    }
                    return new ConcurrentHashMap<>(translations);
        }).computeIfAbsent(key, k -> fallback.apply(locale));
    }

    @Override
    @SuppressWarnings("unchecked") // because: we try catch it
    public @NonNull <T> T serialize(final @NonNull Label label,
                                    final @NonNull Class<T> type) {
        if (!serializerRegistry.containsKey(type))
            throw new IllegalArgumentException("Unsupported serialization type: " + type.getName());

        try {
            return (T) serializerRegistry.get(type).serialize(label);
        } catch (ClassCastException e) {
            throw new IncompatibleMatchException(type, e);
        }
    }

    @Override
    @SuppressWarnings("unchecked") // because: we try catch it
    public <T> @NonNull Label deserialize(final @NonNull T serialized)
            throws ParseException {
        if (!serializerRegistry.containsKey(serialized.getClass()))
            throw new IllegalArgumentException("Unsupported serialization type: " + serialized.getClass().getName());

        try {
            LabelSerializer<T> serializer = (LabelSerializer<T>) serializerRegistry.get(serialized.getClass());
            return serializer.deserialize(serialized);
        } catch (ClassCastException e) {
            throw new IncompatibleMatchException(serialized.getClass(), e);
        }
    }

    @Override
    @SuppressWarnings("unchecked") // because: we try catch it wow what a surprise
    public @NonNull <T> T format(final @NonNull String input,
                                 final @NonNull Class<T> type)
            throws FormatException {
        if (!serializerRegistry.containsKey(type))
            throw new IllegalArgumentException("Unsupported serialization type: " + type.getName());

        try {
            return  (T) serializerRegistry.get(type).format(input);
        } catch (ClassCastException e) {
            throw new IncompatibleMatchException(type, e);
        }
    }

    @Override
    public void clearCache() {
        // note: we can clear sub maps for faster Garbage Collection
        translationCache.clear();
    }

    @Override
    public void clearCache(@NonNull Locale locale) {
        if (translationCache.containsKey(locale.toLanguageTag()))
            translationCache.get(locale.toLanguageTag()).clear();
    }

}
