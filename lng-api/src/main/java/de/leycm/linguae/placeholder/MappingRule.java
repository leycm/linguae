package de.leycm.linguae.placeholder;

import lombok.NonNull;

import java.util.regex.Pattern;

/**
 * MappingRule
 *
 * <p>
 * Defines a pattern for placeholder mapping with prefix and suffix delimiters.
 * Provides commonly used placeholder patterns as static constants and compiles
 * efficient regex patterns for placeholder detection and replacement.
 * </p>
 *
 * <p>Instances are immutable and thread-safe.</p>
 *
 * @author LeyCM
 * @since 1.0.1
 */
public class MappingRule {

    /**
     * Dollar-style placeholder pattern: ${variable}
     */
    public static final @NonNull MappingRule DOLLAR = new MappingRule("${", "}");

    /**
     * Percent-style placeholder pattern: %variable%
     */
    public static final @NonNull MappingRule PERCENT = new MappingRule("%", "%");

    /**
     * F-string style placeholder pattern: %variable
     */
    public static final @NonNull MappingRule FSTRING = new MappingRule("%", "");

    /**
     * Curly brace placeholder pattern: {{variable}}
     */
    public static final @NonNull MappingRule CURLY = new MappingRule("{{", "}}");

    /**
     * MiniMessage style placeholder pattern: <var:variable>
     */
    public static final @NonNull MappingRule MINI_MESSAGE = new MappingRule("<var:", ">");

    private final String prefix;
    private final String suffix;
    private final Pattern pattern;

    /**
     * Constructs a new MappingRule with the specified prefix and suffix.
     *
     * <p>
     * The pattern is compiled to efficiently match placeholders in the format:
     * {@code prefix + content + suffix}. The content is captured as a group and
     * cannot contain the first character of the suffix for proper termination.
     * </p>
     *
     * @param prefix the prefix delimiter for placeholders, cannot be null
     * @param suffix the suffix delimiter for placeholders, cannot be null
     * @throws NullPointerException if prefix or suffix is null
     */
    public MappingRule(final @NonNull String prefix, final @NonNull String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.pattern = Pattern.compile(
                Pattern.quote(prefix) + "([^" + Pattern.quote(suffix.substring(0, 1)) + "]+)" + Pattern.quote(suffix)
        );
    }

    /**
     * Returns the prefix delimiter for this mapping rule.
     *
     * @return the prefix delimiter, never null
     */
    public @NonNull String getPrefix() {
        return prefix;
    }

    /**
     * Returns the suffix delimiter for this mapping rule.
     *
     * @return the suffix delimiter, never null
     */
    public @NonNull String getSuffix() {
        return suffix;
    }

    /**
     * Returns the compiled regex pattern for this mapping rule.
     *
     * <p>
     * The pattern is optimized for matching placeholders that follow the
     * prefix-content-suffix structure defined by this rule.
     * </p>
     *
     * @return the compiled regex pattern, never null
     */
    public @NonNull Pattern getPattern() {
        return pattern;
    }
}