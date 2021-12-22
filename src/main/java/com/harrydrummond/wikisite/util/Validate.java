package com.harrydrummond.wikisite.util;

public class Validate {

    /**
     * Validates the input length of given string using the default max input length value
     *
     * @param input Input string to validate length of
     * @return True if within max length bounds
     */
    public static boolean validateInputLength(final String input) {
        return validateInputLength(input, Options.DEFAULT_MAX_INPUT_LENGTH);
    }

    /**
     * Validates the input length of given string using the given max input length value
     *
     * @param input Input string to validate length of
     * @return True if within max length bounds
     */
    public static boolean validateInputLength(final String input, final int maxLength) {
        return input.length() <= maxLength;
    }

    public static class Options {
        public static final int DEFAULT_MAX_INPUT_LENGTH = 100;
    }
}