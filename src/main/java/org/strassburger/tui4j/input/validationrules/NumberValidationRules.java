package org.strassburger.tui4j.input.validationrules;

import static org.strassburger.tui4j.formatting.TextFormatter.*;

public class NumberValidationRules {

    /**
     * Validates that the input is equal to the given value.
     * @param value The value to compare the input to.
     * @return A ValidationRule that validates that the input is equal to the given value.
     * @param <T> The type of the input.
     */
    public static <T extends Number & Comparable<T>> ValidationRule<T> lessThan(T value) {
        return lessThan(value, "&cThis field must be less than " + value + ".");
    }

    /**
     * Validates that the input is equal to the given value with a custom error message.
     * @param value The value to compare the input to.
     * @param errorMessage The custom error message.
     * @return A ValidationRule that validates that the input is equal to the given value.
     * @param <T> The type of the input.
     */
    public static <T extends Number & Comparable<T>> ValidationRule<T> lessThan(T value, String errorMessage) {
        return new ValidationRule<>() {
            @Override
            public boolean validate(T input) {
                return input.compareTo(value) < 0;
            }

            @Override
            public String getErrorMessage() {
                return format(errorMessage);
            }
        };
    }

    /**
     * Validates that the input is greater than the given value.
     * @param value The value to compare the input to.
     * @return A ValidationRule that validates that the input is greater than the given value.
     * @param <T> The type of the input.
     */
    public static <T extends Number & Comparable<T>> ValidationRule<T> greaterThan(T value) {
        return greaterThan(value, "&cThis field must be greater than " + value + ".");
    }

    /**
     * Validates that the input is greater than the given value with a custom error message.
     * @param value The value to compare the input to.
     * @param errorMessage The custom error message.
     * @return A ValidationRule that validates that the input is greater than the given value.
     * @param <T> The type of the input.
     */
    public static <T extends Number & Comparable<T>> ValidationRule<T> greaterThan(T value, String errorMessage) {
        return new ValidationRule<>() {
            @Override
            public boolean validate(T input) {
                return input.compareTo(value) > 0;
            }

            @Override
            public String getErrorMessage() {
                return format(errorMessage);
            }
        };
    }

    /**
     * Validates that the input is less than or equal to the given value.
     * @param value The value to compare the input to.
     * @return A ValidationRule that validates that the input is less than or equal to the given value.
     * @param <T> The type of the input.
     */
    public static <T extends Number & Comparable<T>> ValidationRule<T> lessThanOrEqualTo(T value) {
        return lessThanOrEqualTo(value, "&cThis field must be less than or equal to " + value + ".");
    }

    /**
     * Validates that the input is less than or equal to the given value with a custom error message.
     * @param value The value to compare the input to.
     * @param errorMessage The custom error message.
     * @return A ValidationRule that validates that the input is less than or equal to the given value.
     * @param <T> The type of the input.
     */
    public static <T extends Number & Comparable<T>> ValidationRule<T> lessThanOrEqualTo(T value, String errorMessage) {
        return new ValidationRule<>() {
            @Override
            public boolean validate(T input) {
                return input.compareTo(value) <= 0;
            }

            @Override
            public String getErrorMessage() {
                return format(errorMessage);
            }
        };
    }

    /**
     * Validates that the input is greater than or equal to the given value.
     * @param value The value to compare the input to.
     * @return A ValidationRule that validates that the input is greater than or equal to the given value.
     * @param <T> The type of the input.
     */
    public static <T extends Number & Comparable<T>> ValidationRule<T> greaterThanOrEqualTo(T value) {
        return greaterThanOrEqualTo(value, "&cThis field must be greater than or equal to " + value + ".");
    }

    /**
     * Validates that the input is greater than or equal to the given value with a custom error message.
     * @param value The value to compare the input to.
     * @param errorMessage The custom error message.
     * @return A ValidationRule that validates that the input is greater than or equal to the given value.
     * @param <T> The type of the input.
     */
    public static <T extends Number & Comparable<T>> ValidationRule<T> greaterThanOrEqualTo(T value, String errorMessage) {
        return new ValidationRule<>() {
            @Override
            public boolean validate(T input) {
                return input.compareTo(value) >= 0;
            }

            @Override
            public String getErrorMessage() {
                return format(errorMessage);
            }
        };
    }
}