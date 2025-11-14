package de.leycm.linguae.placeholder;

import lombok.NonNull;

import java.util.regex.Matcher;

public record Mapping(@NonNull MappingRule rule,
                      @NonNull String key,
                      @NonNull String value) {

    public @NonNull String map(final @NonNull String text) {
        final Matcher matcher = rule.getPattern().matcher(text);
        final StringBuilder result = new StringBuilder(text.length());

        int lastEnd = 0;
        while (matcher.find()) {
            if (!matcher.group(1).equals(key)) continue;
            result.append(text, lastEnd, matcher.start());
            result.append(value);
            lastEnd = matcher.end();
        }

        if (lastEnd == 0) return text;

        result.append(text, lastEnd, text.length());
        return result.toString();
    }

}