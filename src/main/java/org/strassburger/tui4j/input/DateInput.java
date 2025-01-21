package org.strassburger.tui4j.input;

import org.strassburger.tui4j.formatting.Printer;
import org.strassburger.tui4j.input.exceptions.InputValidationException;
import org.strassburger.tui4j.input.exceptions.RetryInputException;
import org.strassburger.tui4j.input.validationrules.ValidationRule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * A class for handling date input
 */
public class DateInput extends Input<Date> {
    private String dateFormat = "yyyy-MM-dd";
    private boolean inline;

    @Override
    public Date read() throws InputValidationException {
        while (true) {
            try {
                Printer.print(getLabel());

                if (inline) System.out.print("");
                else {
                    System.out.println();
                    Printer.print("&8> ");
                }

                String input = getScanner().nextLine().trim();

                Date date = parseDate(input);

                try {
                    validate(date);
                } catch (RetryInputException e) {
                    return read();
                }

                return date;
            } catch (RetryInputException e) {
                if (!isRetryOnInvalid()) {
                    throw new InputValidationException(getErrorMessage());
                }
                Printer.println(getErrorMessage());
            } catch (InputValidationException e) {
                if (!isRetryOnInvalid()) {
                    throw e;
                }
                Printer.println(e.getMessage());
            }
        }
    }

    /**
     * Set the date format for input validation
     * @param dateFormat the date format to use (e.g., "dd/MM/yyyy")
     * @return the DateInput object
     */
    public DateInput setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
        return this;
    }

    /**
     * Get the current date format
     * @return the date format string
     */
    public String getDateFormat() {
        return dateFormat;
    }

    public DateInput setRetryOnInvalid(boolean retryOnInvalid) {
        super.setRetryOnInvalid(retryOnInvalid);
        return this;
    }

    public DateInput setLabel(String label) {
        super.setLabel(label);
        return this;
    }

    public DateInput setErrorMessage(String errorMessage) {
        super.setErrorMessage(errorMessage);
        return this;
    }

    public DateInput setInline(boolean inline) {
        this.inline = inline;
        return this;
    }

    @SafeVarargs
    @Override
    public final DateInput addValidationRules(ValidationRule<Date>... rules) {
        super.addValidationRules(rules);
        return this;
    }

    @Override
    public DateInput addValidationRules(List<ValidationRule<Date>> rules) {
        super.addValidationRules(rules);
        return this;
    }

    /**
     * Parse the input string into a Date object
     * @param input the input string
     * @return the parsed Date object
     * @throws InputValidationException if the input cannot be parsed
     */
    private Date parseDate(String input) throws InputValidationException {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        formatter.setLenient(false);
        try {
            return formatter.parse(input);
        } catch (ParseException e) {
            throw new InputValidationException("&cInvalid date format. Expected format: " + dateFormat);
        }
    }
}
