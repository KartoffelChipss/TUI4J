package org.strassburger.tui4j.input;

import org.strassburger.tui4j.input.exceptions.InputValidationException;
import org.strassburger.tui4j.input.exceptions.RetryInputException;
import org.strassburger.tui4j.printer.Printer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateInput extends Input<LocalDate, LocalDateInput> {
    private String dateFormat = "yyyy-MM-dd";
    private boolean inline;

    public LocalDateInput(Printer printer) {
        super(printer);
    }

    @Override
    public LocalDate read() throws InputValidationException {
        while (true) {
            try {
                printPromptAndCursor(inline);

                String input = getScanner().nextLine().trim();

                LocalDate date = parseDate(input);

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
     * Parses the given input string into a LocalDate using the configured date format.
     * @param input the string to parse
     * @return the parsed LocalDate
     * @throws RetryInputException if the input does not match the configured date format
     */
    private LocalDate parseDate(String input) throws RetryInputException {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
            return LocalDate.parse(input, formatter);
        } catch (DateTimeParseException e) {
            throw new InputValidationException("Invalid date format. Expected format: " + dateFormat);
        }
    }

    public String getDateFormat() {
        return dateFormat;
    }

    /**
     * Set the date format for input validation
     * @param dateFormat the date format to use (e.g., "dd/MM/yyyy")
     * @return the LocalDateInput object
     */
    public LocalDateInput setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
        return this;
    }

    public LocalDateInput setInline(boolean inline) {
        this.inline = inline;
        return this;
    }
}
