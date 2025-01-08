package org.strassburger.input.validationrules;

public class TextValidationRules {
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
