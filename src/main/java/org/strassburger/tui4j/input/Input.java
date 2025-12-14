package org.strassburger.tui4j.input;

import org.strassburger.tui4j.formatting.StyledText;
import org.strassburger.tui4j.formatting.ansi.AnsiColor;
import org.strassburger.tui4j.input.exceptions.InputValidationException;
import org.strassburger.tui4j.input.exceptions.RetryInputException;
import org.strassburger.tui4j.input.validationrules.ValidationRule;
import org.strassburger.tui4j.printer.ConsolePrinter;
import org.strassburger.tui4j.printer.Printer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Abstract class for input
 * @param <T> the type of the input value
 * @param <S> the type of the input subclass
 */
public abstract class Input<T, S extends Input<T, S>> {
    private StyledText prompt = null;
    private boolean retryOnInvalid = true;
    private StyledText errorMessage = StyledText.text("Invalid input. Please try again.").fg(AnsiColor.RED);
    private StyledText cursor = StyledText.text(">").fg(AnsiColor.BRIGHT_BLACK).append(" ").fg(AnsiColor.WHITE);
    private final List<ValidationRule<T>> validationRules;
    private Scanner scanner;
    private Printer printer;

    /**
     * Constructor with default scanner and printer
     */
    public Input() {
        validationRules = new ArrayList<>();
        scanner = new Scanner(System.in);
        printer = new ConsolePrinter();
    }

    /**
     * Read the input value
     * @return the input value
     * @throws InputValidationException if the input is invalid and retryOnInvalid is false
     */
    public abstract T read() throws InputValidationException;

    /**
     * Set the label for the input
     * @param prompt the label to set
     * @return the input object
     */
    @SuppressWarnings("unchecked")
    public S setPrompt(String prompt) {
        this.prompt = StyledText.text(prompt).fg(AnsiColor.BRIGHT_WHITE);
        return (S) this;
    }

    /**
     * Set the label for the input
     * @param label the label to set
     * @return the input object
     * @deprecated Use {@link #setPrompt(String)} instead
     */
    @SuppressWarnings("unchecked")
    public S setLabel(String label) {
        this.prompt = StyledText.text(label).fg(AnsiColor.BRIGHT_WHITE);
        return (S) this;
    }

    /**
     * Set the label for the input
     * @param prompt the label to set
     * @return the input object
     */
    @SuppressWarnings("unchecked")
    public S setPrompt(StyledText prompt) {
        this.prompt = prompt;
        return (S) this;
    }

    /**
     * Set the label for the input
     * @param label the label to set
     * @return the input object
     * @deprecated Use {@link #setPrompt(StyledText)} instead
     */
    @SuppressWarnings("unchecked")
    public S setLabel(StyledText label) {
        this.prompt = label;
        return (S) this;
    }

    /**
     * Set whether to retry on invalid input
     * @param retryOnInvalid whether to retry on invalid input
     * @return the input object
     */
    @SuppressWarnings("unchecked")
    public S setRetryOnInvalid(boolean retryOnInvalid) {
        this.retryOnInvalid = retryOnInvalid;
        return (S) this;
    }

    /**
     * Set the error message for invalid input
     * @param errorMessage the error message to set (as plain text)
     * @return the input object
     */
    @SuppressWarnings("unchecked")
    public S setErrorMessage(String errorMessage) {
        this.errorMessage = StyledText.text(errorMessage).fg(AnsiColor.RED);
        return (S) this;
    }

    /**
     * Set the error message for invalid input
     * @param errorMessage the error message to set
     * @return the input object
     */
    @SuppressWarnings("unchecked")
    public S setErrorMessage(StyledText errorMessage) {
        this.errorMessage = errorMessage;
        return (S) this;
    }

    /**
     * Add a validation rule to the input
     * @param rule the validation rule to add
     * @return the input object
     */
    @SuppressWarnings("unchecked")
    public S addValidationRule(ValidationRule<T> rule) {
        this.validationRules.add(rule);
        return (S) this;
    }

    /**
     * Add validation rules to the input
     * @param rules the validation rules to add
     * @return the input object
     */
    @SuppressWarnings("unchecked")
    public S addValidationRules(List<ValidationRule<T>> rules) {
        validationRules.addAll(rules);
        return (S) this;
    }

    @SuppressWarnings("unchecked")
    public S setPrinter(Printer printer) {
        this.printer = printer;
        return (S) this;
    }

    @SuppressWarnings("unchecked")
    public S setScanner(Scanner scanner) {
        this.scanner = scanner;
        return (S) this;
    }

    @SuppressWarnings("unchecked")
    public S setCursor(StyledText cursor) {
        this.cursor = cursor;
        return (S) this;
    }

    public StyledText getCursor() {
        return cursor;
    }

    /**
     * Validate the input value
     * @param value the input value to validate
     * @throws InputValidationException if the input is invalid
     */
    protected void validate(T value) throws InputValidationException {
        for (ValidationRule<T> rule : validationRules) {
            if (!rule.validate(value)) {
                if (retryOnInvalid) {
                    StyledText errMessage = rule.getErrorMessage() != null ? rule.getErrorMessage() : errorMessage;
                    printer.println(errMessage);
                    throw new RetryInputException(errMessage);
                } else {
                    StyledText errMessage = rule.getErrorMessage() != null ? rule.getErrorMessage() : errorMessage;
                    throw new InputValidationException(errMessage);
                }
            }
        }
    }

    /**
     * @deprecated Use {@link #getPrompt()} instead
     */
    public StyledText getLabel() {
        return prompt;
    }

    public StyledText getPrompt() {
        return prompt;
    }

    public boolean isRetryOnInvalid() {
        return retryOnInvalid;
    }

    public StyledText getErrorMessage() {
        return errorMessage;
    }

    protected Scanner getScanner() {
        return scanner;
    }

    protected Printer getPrinter() {
        return printer;
    }

    protected void printPromptAndCursor(boolean inline) {
        if (getPrompt() != null) getPrinter().print(getPrompt());

        if (inline) getPrinter().print("");
        else {
            if (getPrompt() != null) getPrinter().println();
            getPrinter().print(getCursor());
        }
    }
}