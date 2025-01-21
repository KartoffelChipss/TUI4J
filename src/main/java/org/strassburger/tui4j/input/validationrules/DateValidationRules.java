package org.strassburger.tui4j.input.validationrules;

import java.util.Date;

public class DateValidationRules {

    /**
     * Returns a validation rule that checks if the date is in the future.
     * @return A validation rule that checks if the date is in the future.
     */
    public static ValidationRule<Date> futureDate() {
        return new ValidationRule<Date>() {
            @Override
            public boolean validate(Date input) {
                return input.after(new Date());
            }

            @Override
            public String getErrorMessage() {
                return "&cThe date must be in the future.";
            }
        };
    }

    /**
     * Returns a validation rule that checks if the date is in the past.
     * @return A validation rule that checks if the date is in the past.
     */
    public static ValidationRule<Date> pastDate() {
        return new ValidationRule<Date>() {
            @Override
            public boolean validate(Date input) {
                return input.before(new Date());
            }

            @Override
            public String getErrorMessage() {
                return "&cThe date must be in the past.";
            }
        };
    }

    /**
     * Returns a validation rule that checks if the date is before a certain date.
     * @param date The date to compare against.
     * @return A validation rule that checks if the date is before a certain date.
     */
    public static ValidationRule<Date> after(Date date) {
        return new ValidationRule<Date>() {
            @Override
            public boolean validate(Date input) {
                return input.after(date);
            }

            @Override
            public String getErrorMessage() {
                return "&cThe date must be after " + date + ".";
            }
        };
    }

    /**
     * Returns a validation rule that checks if the date is after a certain date.
     * @param date The date to compare against.
     * @return A validation rule that checks if the date is after a certain date.
     */
    public static ValidationRule<Date> before(Date date) {
        return new ValidationRule<Date>() {
            @Override
            public boolean validate(Date input) {
                return input.before(date);
            }

            @Override
            public String getErrorMessage() {
                return "&cThe date must be before " + date + ".";
            }
        };
    }
}
