package org.strassburger.input.validationrules;

public class TextValidationRules {
    /**
     * Returns a validation rule that checks if the input contains spaces.
     * @return A validation rule that checks if the input contains spaces.
     */
    public static ValidationRule<String> noSpaces() {
        return new ValidationRule<>() {
            @Override
            public boolean validate(String input) {
                return !input.contains(" ");
            }

            @Override
            public String getErrorMessage() {
                return "This field cannot contain spaces.";
            }
        };
    }

    /**
     * Returns a validation rule that checks if the input is at least a certain length.
     * @param length The minimum length of the input.
     * @return A validation rule that checks if the input is at least a certain length.
     */
    public static ValidationRule<String> minLength(int length) {
        return new ValidationRule<>() {
            @Override
            public boolean validate(String input) {
                return input.length() >= length;
            }

            @Override
            public String getErrorMessage() {
                return "This field must be at least " + length + " characters long.";
            }
        };
    }

    /**
     * Returns a validation rule that checks if the input is at most a certain length.
     * @param length The maximum length of the input.
     * @return A validation rule that checks if the input is at most a certain length.
     */
    public static ValidationRule<String> maxLength(int length) {
        return new ValidationRule<>() {
            @Override
            public boolean validate(String input) {
                return input.length() <= length;
            }

            @Override
            public String getErrorMessage() {
                return "This field must be at most " + length + " characters long.";
            }
        };
    }

    /**
     * Returns a validation rule that checks if the input is between a certain length.
     * @return A validation rule that checks if the input is between a certain length.
     */
    public static ValidationRule<String> disallowEmpty() {
        return new ValidationRule<>() {
            @Override
            public boolean validate(String input) {
                return !input.isEmpty();
            }

            @Override
            public String getErrorMessage() {
                return "This field cannot be empty.";
            }
        };
    }

    /**
     * Returns a validation rule that checks if the input matches a certain regex.
     * @param regex The regex to match.
     * @return A validation rule that checks if the input matches a certain regex.
     */
    public static ValidationRule<String> regex(String regex) {
        return new ValidationRule<>() {
            @Override
            public boolean validate(String input) {
                return input.matches(regex);
            }

            @Override
            public String getErrorMessage() {
                return "This field must match the following regex: " + regex;
            }
        };
    }

    /**
     * Returns a validation rule that checks if the input does not contain numbers.
     * @return A validation rule that checks if the input does not contain numbers.
     */
    public static ValidationRule<String> noNumbers() {
        return new ValidationRule<>() {
            @Override
            public boolean validate(String input) {
                return !input.matches(".*\\d.*");
            }

            @Override
            public String getErrorMessage() {
                return "This field cannot contain numbers.";
            }
        };
    }

    /**
     * Returns a validation rule that checks if the input is alphanumeric.
     * @return A validation rule that checks if the input is alphanumeric.
     */
    public static ValidationRule<String> alphanumeric() {
        return new ValidationRule<>() {
            @Override
            public boolean validate(String input) {
                return input.matches("^[a-zA-Z0-9]*$");
            }

            @Override
            public String getErrorMessage() {
                return "This field must be alphanumeric.";
            }
        };
    }
}
