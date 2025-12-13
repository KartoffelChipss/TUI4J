package org.strassburger.tui4j.input;

import org.strassburger.tui4j.formatting.StyledText;
import org.strassburger.tui4j.formatting.ansi.AnsiColor;
import org.strassburger.tui4j.input.exceptions.InputValidationException;
import org.strassburger.tui4j.input.exceptions.RetryInputException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A class for handling date input
 */
public class DateInput extends Input<Date, DateInput> {
    private String dateFormat = "yyyy-MM-dd";
    private boolean inline;

    @Override
    public Date read() throws InputValidationException {
        while (true) {
            try {
                getPrinter().print(getLabel());

                if (inline) getPrinter().print("");
                else {
                    getPrinter().println();
                    getPrinter().print(StyledText.text("> ").fg(AnsiColor.BRIGHT_BLACK));
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
                getPrinter().println(getErrorMessage());
            } catch (InputValidationException e) {
                if (!isRetryOnInvalid()) {
                    throw e;
                }
                getPrinter().println(e.getMessage());
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

    public DateInput setInline(boolean inline) {
        this.inline = inline;
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
