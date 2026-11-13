package org.strassburger.tui4j.input;

import org.jspecify.annotations.Nullable;
import org.strassburger.tui4j.console.Console;
import org.strassburger.tui4j.formatting.StyledText;
import org.strassburger.tui4j.formatting.ansi.AnsiColor;
import org.strassburger.tui4j.input.exceptions.InputValidationException;
import org.strassburger.tui4j.input.validationrules.ValidationRule;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class for input
 * @param <T> the type of the input value
 * @param <S> the type of the input subclass
 */
public abstract class Input<T, S extends Input<T, S>> {
    private @Nullable StyledText prompt = null;
    private boolean retryOnInvalid = true;
    private StyledText errorMessage = StyledText.text("Invalid input. Please try again.").fg(AnsiColor.RED);
    private StyledText cursor = StyledText.text(">").fg(AnsiColor.BRIGHT_BLACK).append(" ").fg(AnsiColor.WHITE);
    private final List<ValidationRule<T>> validationRules = new ArrayList<>();

    /**
     * Read the input value. Prints the prompt, reads the raw input, parses and validates it.
     * If the input is invalid and {@link #isRetryOnInvalid()} is true, the error message is printed
     * and the user is asked again.
     * @param console the console instance used to read the input
     * @return the input value
     * @throws InputValidationException if the input is invalid and retryOnInvalid is false
     */
    public T read(Console console) throws InputValidationException {
        while (true) {
            printPrompt(console);
            try {
                T value = parse(readInput(console));
                validate(value);
                return value;
            } catch (InputValidationException e) {
                if (!retryOnInvalid) throw e;
                console.println(e.getStyledMessage());
            }
        }
    }

    /**
     * Print the prompt before reading the input. Defaults to the prompt followed by the cursor on a new line.
     * @param console the console to print to
     */
    protected void printPrompt(Console console) {
        printPromptAndCursor(console, false);
    }

    /**
     * Read the raw input. Defaults to a single line.
     * @param console the console to read from
     * @return the raw input
     */
    protected String readInput(Console console) {
        return console.readLine();
    }

    /**
     * Convert the raw input into the input value
     * @param input the raw input
     * @return the parsed value
     * @throws InputValidationException if the input cannot be parsed
     */
    protected abstract T parse(String input) throws InputValidationException;

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
     * @param prompt the label to set
     * @return the input object
     */
    @SuppressWarnings("unchecked")
    public S setPrompt(StyledText prompt) {
        this.prompt = prompt;
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
    public S setCursor(StyledText cursor) {
        this.cursor = cursor;
        return (S) this;
    }

    public StyledText getCursor() {
        return cursor;
    }

    /**
     * Validate the input value against all validation rules
     * @param value the input value to validate
     * @throws InputValidationException if the input is invalid
     */
    protected void validate(T value) throws InputValidationException {
        for (ValidationRule<T> rule : validationRules) {
            if (!rule.validate(value)) {
                StyledText ruleMessage = rule.getErrorMessage();
                throw new InputValidationException(ruleMessage != null ? ruleMessage : errorMessage);
            }
        }
    }

    /**
     * Create an exception for input that cannot be parsed, using the configured error message
     * @return the exception to throw
     */
    protected InputValidationException invalidInput() {
        return new InputValidationException(errorMessage);
    }

    public @Nullable StyledText getPrompt() {
        return prompt;
    }

    public boolean isRetryOnInvalid() {
        return retryOnInvalid;
    }

    public StyledText getErrorMessage() {
        return errorMessage;
    }

    protected void printPromptAndCursor(Console console, boolean inline) {
        if (getPrompt() != null) console.print(getPrompt());

        if (inline) console.print("");
        else {
            if (getPrompt() != null) console.println();
            console.print(getCursor());
        }
    }
}