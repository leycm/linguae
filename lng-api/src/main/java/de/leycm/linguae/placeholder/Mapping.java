package de.leycm.linguae.placeholder;

import lombok.NonNull;

import java.util.function.Supplier;
import java.util.regex.Matcher;

public record Mapping(@NonNull PsPattern rule,
                      @NonNull String key,
                      @NonNull Supplier<String> value
) {

    public @NonNull String map(final @NonNull String text) {
        final Matcher matcher = rule.getPattern().matcher(text);
        final StringBuilder result = new StringBuilder(text.length());

        int lastEnd = 0;
        while (matcher.find()) {
            if (!matcher.group(1).equals(key)) continue;
            result.append(text, lastEnd, matcher.start());
            result.append(value.get());
            lastEnd = matcher.end();
        }

        if (lastEnd == 0) return text;

        result.append(text, lastEnd, text.length());
        return result.toString();
    }

}