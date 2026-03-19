/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.debezium.connector.mysql.antlr;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Converts double-quoted string literals to single-quoted strings in MySQL DDL.
 *
 * The Oracle MySQL grammar uses ANSI_QUOTES mode (double quotes = identifiers),
 * but MySQL's default mode allows double quotes for strings. This normalizer
 * transforms DDL before parsing to support both modes.
 */
public class QuoteNormalizer {

    // Matches double-quoted strings with escape sequences
    private static final Pattern DOUBLE_QUOTED_STRING = Pattern.compile(
            "\"([^\"\\\\]*(?:\\\\.[^\"\\\\]*)*)\"",
            Pattern.DOTALL);

    /**
     * Converts double-quoted strings to single-quoted strings.
     *
     * @param ddlContent The DDL statement to normalize
     * @return Normalized DDL with single quotes for string literals
     */
    public static String normalize(String ddlContent) {
        if (ddlContent == null || ddlContent.isEmpty()) {
            return ddlContent;
        }

        Matcher matcher = DOUBLE_QUOTED_STRING.matcher(ddlContent);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String content = matcher.group(1);

            // Convert escape sequences: \" → \', preserve existing ''
            String normalized = content
                    .replace("\\\"", "\\'")
                    .replace("''", "\\'\\'");

            matcher.appendReplacement(result, "'" + Matcher.quoteReplacement(normalized) + "'");
        }
        matcher.appendTail(result);

        return result.toString();
    }
}
