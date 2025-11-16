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
package de.leycm.linguae.placeholder;

import lombok.NonNull;

import java.util.regex.Pattern;

/**
 * Defines a pattern for placeholder mapping with prefix and suffix delimiters.
 *
 * <p>Provides commonly used placeholder patterns as static constants and compiles
 * efficient regex patterns for placeholder detection and replacement.</p>
 *
 * <p>Instances are immutable and thread-safe.</p>
 *
 * @author LeyCM
 * @since 1.0.1
 */
public class PsPattern {

    /**
     * Dollar-style placeholder pattern: {@code ${variable}}
     */
    public static final @NonNull PsPattern DOLLAR = new PsPattern("${", "}");

    /**
     * Percent-style placeholder pattern: {@code %variable%}
     */
    public static final @NonNull PsPattern PERCENT = new PsPattern("%", "%");

    /**
     * F-string style placeholder pattern: {@code %variable}
     */
    public static final @NonNull PsPattern FSTRING = new PsPattern("%", "");

    /**
     * Curly brace placeholder pattern: {@code {{variable}}}
     */
    public static final @NonNull PsPattern CURLY = new PsPattern("{{", "}}");

    /**
     * MiniMessage style placeholder pattern: {@code <var:variable>}
     */
    public static final @NonNull PsPattern MINI_MESSAGE = new PsPattern("<var:", ">");

    private final String prefix;
    private final String suffix;
    private final Pattern pattern;

    /**
     * Constructs a new PsPattern with the specified prefix and suffix.
     *
     * <p>The pattern is compiled to efficiently match placeholders in the format:
     * {@code prefix + content + suffix}. The content is captured as a group and
     * cannot contain the first character of the suffix for proper termination.</p>
     *
     * @param prefix the prefix delimiter for placeholders
     * @param suffix the suffix delimiter for placeholders
     * @throws NullPointerException if prefix or suffix is null
     */
    public PsPattern(final @NonNull String prefix, final @NonNull String suffix) {
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
     * <p>The pattern is optimized for matching placeholders that follow the
     * prefix-content-suffix structure defined by this rule.</p>
     *
     * @return the compiled regex pattern, never null
     */
    public @NonNull Pattern getPattern() {
        return pattern;
    }
}