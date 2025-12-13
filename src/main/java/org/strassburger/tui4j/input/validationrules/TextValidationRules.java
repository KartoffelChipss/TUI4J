package org.strassburger.tui4j.input.validationrules;

import org.strassburger.tui4j.formatting.StyledText;
import org.strassburger.tui4j.formatting.ansi.AnsiColor;

public class TextValidationRules {
    /**
     * Returns a validation rule that checks if the input contains spaces.
     * @return A validation rule that checks if the input contains spaces.
     */
    public static ValidationRule<String> noSpaces() {
        return noSpaces("This field cannot contain spaces.");
    }

    /**
     * Returns a validation rule that checks if the input contains spaces with a custom error message.
     * @param errorMessage The custom error message.
     * @return A validation rule that checks if the input contains spaces.
     */
    public static ValidationRule<String> noSpaces(String errorMessage) {
        return noSpaces(StyledText.text(errorMessage).fg(AnsiColor.RED));
    }

    /**
     * Returns a validation rule that checks if the input contains spaces with a custom error message.
     * @param errorMessage The custom error message.
     * @return A validation rule that checks if the input contains spaces.
     */
    public static ValidationRule<String> noSpaces(StyledText errorMessage) {
        return new ValidationRule<>() {
            @Override
            public boolean validate(String input) {
                return !input.contains(" ");
            }

            @Override
            public StyledText getErrorMessage() {
                return errorMessage;
            }
        };
    }

    /**
     * Returns a validation rule that checks if the input is at least a certain length.
     * @param length The minimum length of the input.
     * @return A validation rule that checks if the input is at least a certain length.
     */
    public static ValidationRule<String> minLength(int length) {
        return minLength(length, StyledText.text("This field must be at least " + length + " characters long.").fg(AnsiColor.RED));
    }

    /**
     * Returns a validation rule that checks if the input is at least a certain length with a custom string error message.
     * @param length The minimum length of the input.
     * @param errorMessage The custom error message.
     * @return A validation rule that checks if the input is at least a certain length.
     */
    public static ValidationRule<String> minLength(int length, String errorMessage) {
        return minLength(length, StyledText.text(errorMessage).fg(AnsiColor.RED));
    }

    /**
     * Returns a validation rule that checks if the input is at least a certain length with a custom error message.
     * @param length The minimum length of the input.
     * @param errorMessage The custom error message.
     * @return A validation rule that checks if the input is at least a certain length.
     */
    public static ValidationRule<String> minLength(int length, StyledText errorMessage) {
        return new ValidationRule<>() {
            @Override
            public boolean validate(String input) {
                return input.length() >= length;
            }

            @Override
            public StyledText getErrorMessage() {
                return errorMessage;
            }
        };
    }

    /**
     * Returns a validation rule that checks if the input is at most a certain length.
     * @param length The maximum length of the input.
     * @return A validation rule that checks if the input is at most a certain length.
     */
    public static ValidationRule<String> maxLength(int length) {
        return maxLength(length, StyledText.text("This field must be at most " + length + " characters long.").fg(AnsiColor.RED));
    }

    /**
     * Returns a validation rule that checks if the input is at most a certain length with a custom string error message.
     * @param length The maximum length of the input.
     * @param errorMessage The custom error message.
     * @return A validation rule that checks if the input is at most a certain length.
     */
    public static ValidationRule<String> maxLength(int length, String errorMessage) {
        return maxLength(length, StyledText.text(errorMessage).fg(AnsiColor.RED));
    }

    /**
     * Returns a validation rule that checks if the input is at most a certain length with a custom error message.
     * @param length The maximum length of the input.
     * @param errorMessage The custom error message.
     * @return A validation rule that checks if the input is at most a certain length.
     */
    public static ValidationRule<String> maxLength(int length, StyledText errorMessage) {
        return new ValidationRule<>() {
            @Override
            public boolean validate(String input) {
                return input.length() <= length;
            }

            @Override
            public StyledText getErrorMessage() {
                return errorMessage;
            }
        };
    }

    /**
     * Returns a validation rule that checks if the input is not empty.
     * @return A validation rule that checks if the input is not empty.
     */
    public static ValidationRule<String> disallowEmpty() {
        return disallowEmpty(StyledText.text("This field cannot be empty.").fg(AnsiColor.RED));
    }

    /**
     * Returns a validation rule that checks if the input is not empty with a custom string error message.
     * @param errorMessage The custom error message.
     * @return A validation rule that checks if the input is not empty.
     */
    public static ValidationRule<String> disallowEmpty(String errorMessage) {
        return disallowEmpty(StyledText.text(errorMessage).fg(AnsiColor.RED));
    }

    /**
     * Returns a validation rule that checks if the input is not empty with a custom error message.
     * @param errorMessage The custom error message.
     * @return A validation rule that checks if the input is not empty.
     */
    public static ValidationRule<String> disallowEmpty(StyledText errorMessage) {
        return new ValidationRule<>() {
            @Override
            public boolean validate(String input) {
                return !input.isEmpty();
            }

            @Override
            public StyledText getErrorMessage() {
                return errorMessage;
            }
        };
    }

    /**
     * Returns a validation rule that checks if the input matches a certain regex.
     * @param regex The regex to match.
     * @return A validation rule that checks if the input matches a certain regex.
     */
    public static ValidationRule<String> regex(String regex) {
        return regex(regex, StyledText.text("This field must match the regex: " + regex).fg(AnsiColor.RED));
    }

    /**
     * Returns a validation rule that checks if the input matches a certain regex with a custom string error message.
     * @param regex The regex to match.
     * @param errorMessage The custom error message.
     * @return A validation rule that checks if the input matches a certain regex.
     */
    public static ValidationRule<String> regex(String regex, String errorMessage) {
        return regex(regex, StyledText.text(errorMessage).fg(AnsiColor.RED));
    }

    /**
     * Returns a validation rule that checks if the input matches a certain regex with a custom error message.
     * @param regex The regex to match.
     * @param errorMessage The custom error message.
     * @return A validation rule that checks if the input matches a certain regex.
     */
    public static ValidationRule<String> regex(String regex, StyledText errorMessage) {
        return new ValidationRule<>() {
            @Override
            public boolean validate(String input) {
                return input.matches(regex);
            }

            @Override
            public StyledText getErrorMessage() {
                return errorMessage;
            }
        };
    }

    /**
     * Returns a validation rule that checks if the input does not contain numbers.
     * @return A validation rule that checks if the input does not contain numbers.
     */
    public static ValidationRule<String> noNumbers() {
        return noNumbers(StyledText.text("This field cannot contain numbers.").fg(AnsiColor.RED));
    }

    /**
     * Returns a validation rule that checks if the input does not contain numbers with a custom string error message.
     * @param errorMessage The custom error message.
     * @return A validation rule that checks if the input does not contain numbers.
     */
    public static ValidationRule<String> noNumbers(String errorMessage) {
        return noNumbers(StyledText.text(errorMessage).fg(AnsiColor.RED));
    }

    /**
     * Returns a validation rule that checks if the input does not contain numbers with a custom error message.
     * @param errorMessage The custom error message.
     * @return A validation rule that checks if the input does not contain numbers.
     */
    public static ValidationRule<String> noNumbers(StyledText errorMessage) {
        return new ValidationRule<>() {
            @Override
            public boolean validate(String input) {
                return !input.matches(".*\\d.*");
            }

            @Override
            public StyledText getErrorMessage() {
                return errorMessage;
            }
        };
    }

    /**
     * Returns a validation rule that checks if the input is alphanumeric.
     * @return A validation rule that checks if the input is alphanumeric.
     */
    public static ValidationRule<String> alphanumeric() {
        return alphanumeric(StyledText.text("This field must be alphanumeric.").fg(AnsiColor.RED));
    }

    /**
     * Returns a validation rule that checks if the input is alphanumeric with a custom string error message.
     * @param errorMessage The custom error message.
     * @return A validation rule that checks if the input is alphanumeric.
     */
    public static ValidationRule<String> alphanumeric(String errorMessage) {
        return alphanumeric(StyledText.text(errorMessage).fg(AnsiColor.RED));
    }

    /**
     * Returns a validation rule that checks if the input is alphanumeric with a custom error message.
     * @param errorMessage The custom error message.
     * @return A validation rule that checks if the input is alphanumeric.
     */
    public static ValidationRule<String> alphanumeric(StyledText errorMessage) {
        return new ValidationRule<>() {
            @Override
            public boolean validate(String input) {
                return input.matches("^[a-zA-Z0-9]*$");
            }

            @Override
            public StyledText getErrorMessage() {
                return errorMessage;
            }
        };
    }
}