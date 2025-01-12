package org.strassburger.tui4j.input.validationrules;

import static org.strassburger.tui4j.formatting.TextFormatter.*;

public class TextValidationRules {
    /**
     * Returns a validation rule that checks if the input contains spaces.
     * @return A validation rule that checks if the input contains spaces.
     */
    public static ValidationRule<String> noSpaces() {
        return noSpaces("&cThis field cannot contain spaces.");
    }

    /**
     * Returns a validation rule that checks if the input contains spaces with a custom error message.
     * @param errorMessage The custom error message.
     * @return A validation rule that checks if the input contains spaces.
     */
    public static ValidationRule<String> noSpaces(String errorMessage) {
        return new ValidationRule<>() {
            @Override
            public boolean validate(String input) {
                return !input.contains(" ");
            }

            @Override
            public String getErrorMessage() {
                return format(errorMessage);
            }
        };
    }

    /**
     * Returns a validation rule that checks if the input is at least a certain length.
     * @param length The minimum length of the input.
     * @return A validation rule that checks if the input is at least a certain length.
     */
    public static ValidationRule<String> minLength(int length) {
        return minLength(length, "&cThis field must be at least " + length + " characters long.");
    }

    /**
     * Returns a validation rule that checks if the input is at least a certain length with a custom error message.
     * @param length The minimum length of the input.
     * @param errorMessage The custom error message.
     * @return A validation rule that checks if the input is at least a certain length.
     */
    public static ValidationRule<String> minLength(int length, String errorMessage) {
        return new ValidationRule<>() {
            @Override
            public boolean validate(String input) {
                return input.length() >= length;
            }

            @Override
            public String getErrorMessage() {
                return format(errorMessage);
            }
        };
    }

    /**
     * Returns a validation rule that checks if the input is at most a certain length.
     * @param length The maximum length of the input.
     * @return A validation rule that checks if the input is at most a certain length.
     */
    public static ValidationRule<String> maxLength(int length) {
        return maxLength(length, "&cThis field must be at most " + length + " characters long.");
    }

    /**
     * Returns a validation rule that checks if the input is at most a certain length with a custom error message.
     * @param length The maximum length of the input.
     * @param errorMessage The custom error message.
     * @return A validation rule that checks if the input is at most a certain length.
     */
    public static ValidationRule<String> maxLength(int length, String errorMessage) {
        return new ValidationRule<>() {
            @Override
            public boolean validate(String input) {
                return input.length() <= length;
            }

            @Override
            public String getErrorMessage() {
                return format(errorMessage);
            }
        };
    }

    /**
     * Returns a validation rule that checks if the input is not empty.
     * @return A validation rule that checks if the input is not empty.
     */
    public static ValidationRule<String> disallowEmpty() {
        return disallowEmpty("&cThis field cannot be empty.");
    }

    /**
     * Returns a validation rule that checks if the input is not empty with a custom error message.
     * @param errorMessage The custom error message.
     * @return A validation rule that checks if the input is not empty.
     */
    public static ValidationRule<String> disallowEmpty(String errorMessage) {
        return new ValidationRule<>() {
            @Override
            public boolean validate(String input) {
                return !input.isEmpty();
            }

            @Override
            public String getErrorMessage() {
                return format(errorMessage);
            }
        };
    }

    /**
     * Returns a validation rule that checks if the input matches a certain regex.
     * @param regex The regex to match.
     * @return A validation rule that checks if the input matches a certain regex.
     */
    public static ValidationRule<String> regex(String regex) {
        return regex(regex, "&cThis field must match the regex: " + regex);
    }

    /**
     * Returns a validation rule that checks if the input matches a certain regex with a custom error message.
     * @param regex The regex to match.
     * @param errorMessage The custom error message.
     * @return A validation rule that checks if the input matches a certain regex.
     */
    public static ValidationRule<String> regex(String regex, String errorMessage) {
        return new ValidationRule<>() {
            @Override
            public boolean validate(String input) {
                return input.matches(regex);
            }

            @Override
            public String getErrorMessage() {
                return format(errorMessage);
            }
        };
    }

    /**
     * Returns a validation rule that checks if the input does not contain numbers.
     * @return A validation rule that checks if the input does not contain numbers.
     */
    public static ValidationRule<String> noNumbers() {
        return noNumbers("&cThis field cannot contain numbers.");
    }

    /**
     * Returns a validation rule that checks if the input does not contain numbers with a custom error message.
     * @param errorMessage The custom error message.
     * @return A validation rule that checks if the input does not contain numbers.
     */
    public static ValidationRule<String> noNumbers(String errorMessage) {
        return new ValidationRule<>() {
            @Override
            public boolean validate(String input) {
                return !input.matches(".*\\d.*");
            }

            @Override
            public String getErrorMessage() {
                return format(errorMessage);
            }
        };
    }

    /**
     * Returns a validation rule that checks if the input is alphanumeric.
     * @return A validation rule that checks if the input is alphanumeric.
     */
    public static ValidationRule<String> alphanumeric() {
        return alphanumeric("&cThis field must be alphanumeric.");
    }

    /**
     * Returns a validation rule that checks if the input is alphanumeric with a custom error message.
     * @param errorMessage The custom error message.
     * @return A validation rule that checks if the input is alphanumeric.
     */
    public static ValidationRule<String> alphanumeric(String errorMessage) {
        return new ValidationRule<>() {
            @Override
            public boolean validate(String input) {
                return input.matches("^[a-zA-Z0-9]*$");
            }

            @Override
            public String getErrorMessage() {
                return format(errorMessage);
            }
        };
    }
}