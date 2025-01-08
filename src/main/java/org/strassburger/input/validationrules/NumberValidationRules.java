package org.strassburger.input.validationrules;

public class NumberValidationRules {
    public static <T extends Number & Comparable<T>> ValidationRule<T> lessThan(T value) {
        return new ValidationRule<>() {
            @Override
            public boolean validate(T input) {
                return input.compareTo(value) < 0;
            }

            @Override
            public String getErrorMessage() {
                return "This field must be less than " + value + ".";
            }
        };
    }

    public static <T extends Number & Comparable<T>> ValidationRule<T> greaterThan(T value) {
        return new ValidationRule<>() {
            @Override
            public boolean validate(T input) {
                return input.compareTo(value) > 0;
            }

            @Override
            public String getErrorMessage() {
                return "This field must be greater than " + value + ".";
            }
        };
    }

    public static <T extends Number & Comparable<T>> ValidationRule<T> lessThanOrEqualTo(T value) {
        return new ValidationRule<>() {
            @Override
            public boolean validate(T input) {
                return input.compareTo(value) <= 0;
            }

            @Override
            public String getErrorMessage() {
                return "This field must be less than or equal to " + value + ".";
            }
        };
    }

    public static <T extends Number & Comparable<T>> ValidationRule<T> greaterThanOrEqualTo(T value) {
        return new ValidationRule<>() {
            @Override
            public boolean validate(T input) {
                return input.compareTo(value) >= 0;
            }

            @Override
            public String getErrorMessage() {
                return "This field must be greater than or equal to " + value + ".";
            }
        };
    }
}