package org.strassburger.tui4j.input.validationrules;

import org.strassburger.tui4j.formatting.StyledText;
import org.strassburger.tui4j.formatting.ansi.AnsiColor;

import java.util.Date;

public class DateValidationRules {

    /**
     * Returns a validation rule that checks if the date is in the future.
     * @return A validation rule that checks if the date is in the future.
     */
    public static ValidationRule<Date> futureDate() {
        return futureDate("The date must be in the future.");
    }

    /**
     * Returns a validation rule that checks if the date is in the future with a custom string error message.
     * @param errorMessage The error message to display if the validation fails.
     * @return A validation rule that checks if the date is in the future.
     */
    public static ValidationRule<Date> futureDate(String errorMessage) {
        return futureDate(StyledText.text(errorMessage).fg(AnsiColor.RED));
    }

    /**
     * Returns a validation rule that checks if the date is in the future with a custom styled error message.
     * @param errorMessage The styled error message to display if the validation fails.
     * @return A validation rule that checks if the date is in the future.
     */
    public static ValidationRule<Date> futureDate(StyledText errorMessage) {
        return new ValidationRule<>() {
            @Override
            public boolean validate(Date input) {
                return input.after(new Date());
            }

            @Override
            public StyledText getErrorMessage() {
                return errorMessage;
            }
        };
    }


    /**
     * Returns a validation rule that checks if the date is in the past.
     * @return A validation rule that checks if the date is in the past.
     */
    public static ValidationRule<Date> pastDate() {
        return pastDate("The date must be in the past.");
    }

    /**
     * Returns a validation rule that checks if the date is in the past with a custom string error message.
     * @param errorMessage The error message to display if the validation fails.
     * @return A validation rule that checks if the date is in the past.
     */
    public static ValidationRule<Date> pastDate(String errorMessage) {
        return pastDate(StyledText.text(errorMessage).fg(AnsiColor.RED));
    }

    /**
     * Returns a validation rule that checks if the date is in the past with a custom styled error message.
     * @param errorMessage The styled error message to display if the validation fails.
     * @return A validation rule that checks if the date is in the past.
     */
    public static ValidationRule<Date> pastDate(StyledText errorMessage) {
        return new ValidationRule<>() {
            @Override
            public boolean validate(Date input) {
                return input.before(new Date());
            }

            @Override
            public StyledText getErrorMessage() {
                return errorMessage;
            }
        };
    }


    /**
     * Returns a validation rule that checks if the date is after a specified date.
     * @param date The date to compare against.
     * @return A validation rule that checks if the date is after the specified date.
     */
    public static ValidationRule<Date> after(Date date) {
        return after(date, "The date must be after " + date + ".");
    }

    /**
     * Returns a validation rule that checks if the date is after a specified date with a custom string error message.
     * @param date The date to compare against.
     * @param errorMessage The error message to display if the validation fails.
     * @return A validation rule that checks if the date is after the specified date.
     */
    public static ValidationRule<Date> after(Date date, String errorMessage) {
        return after(date, StyledText.text(errorMessage).fg(AnsiColor.RED));
    }

    /**
     * Returns a validation rule that checks if the date is after a specified date with a custom styled error message.
     * @param date The date to compare against.
     * @param errorMessage The styled error message to display if the validation fails.
     * @return A validation rule that checks if the date is after the specified date.
     */
    public static ValidationRule<Date> after(Date date, StyledText errorMessage) {
        return new ValidationRule<>() {
            @Override
            public boolean validate(Date input) {
                return input.after(date);
            }

            @Override
            public StyledText getErrorMessage() {
                return errorMessage;
            }
        };
    }


    /**
     * Returns a validation rule that checks if the date is before a specified date.
     * @param date The date to compare against.
     * @return A validation rule that checks if the date is before the specified date.
     */
    public static ValidationRule<Date> before(Date date) {
        return before(date, "The date must be before " + date + ".");
    }

    /**
     * Returns a validation rule that checks if the date is before a specified date with a custom string error message.
     * @param date The date to compare against.
     * @param errorMessage The error message to display if the validation fails.
     * @return A validation rule that checks if the date is before the specified date.
     */
    public static ValidationRule<Date> before(Date date, String errorMessage) {
        return before(date, StyledText.text(errorMessage).fg(AnsiColor.RED));
    }

    /**
     * Returns a validation rule that checks if the date is before a specified date with a custom styled error message.
     * @param date The date to compare against.
     * @param errorMessage The styled error message to display if the validation fails.
     * @return A validation rule that checks if the date is before the specified date.
     */
    public static ValidationRule<Date> before(Date date, StyledText errorMessage) {
        return new ValidationRule<>() {
            @Override
            public boolean validate(Date input) {
                return input.before(date);
            }

            @Override
            public StyledText getErrorMessage() {
                return errorMessage;
            }
        };
    }
}