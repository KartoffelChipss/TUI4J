package org.strassburger.tui4j.input;

import org.strassburger.tui4j.console.Console;
import org.strassburger.tui4j.formatting.StyledText;
import org.strassburger.tui4j.input.exceptions.InputValidationException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateInput extends Input<LocalDate, DateInput> {

    /**
     * Create a new DateInput with the given prompt
     * @param prompt the prompt to display
     * @return the new DateInput
     */
    public static DateInput withPrompt(String prompt) {
        return new DateInput().prompt(prompt);
    }

    /**
     * Create a new DateInput with the given prompt
     * @param prompt the prompt to display
     * @return the new DateInput
     */
    public static DateInput withPrompt(StyledText prompt) {
        return new DateInput().prompt(prompt);
    }

    private String dateFormat = "yyyy-MM-dd";
    private boolean inline;

    @Override
    protected void printPrompt(Console console) {
        printPromptAndCursor(console, inline);
    }

    /**
     * Parses the given input string into a LocalDate using the configured date format.
     * @param input the string to parse
     * @return the parsed LocalDate
     * @throws InputValidationException if the input does not match the configured date format
     */
    @Override
    protected LocalDate parse(String input) throws InputValidationException {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
            return LocalDate.parse(input.trim(), formatter);
        } catch (DateTimeParseException e) {
            throw new InputValidationException("Invalid date format. Expected format: " + dateFormat);
        }
    }

    public String dateFormat() {
        return dateFormat;
    }

    /**
     * Set the date format for input validation
     * @param dateFormat the date format to use (e.g., "dd/MM/yyyy")
     * @return the DateInput object
     */
    public DateInput dateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
        return this;
    }

    public DateInput inline(boolean inline) {
        this.inline = inline;
        return this;
    }

    public boolean inline() {
        return inline;
    }
}
