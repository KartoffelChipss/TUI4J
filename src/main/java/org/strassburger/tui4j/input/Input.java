package org.strassburger.tui4j.input;

import org.strassburger.tui4j.formatting.Printer;
import org.strassburger.tui4j.formatting.TextFormatter;
import org.strassburger.tui4j.input.exceptions.InputValidationException;
import org.strassburger.tui4j.input.exceptions.RetryInputException;
import org.strassburger.tui4j.input.validationrules.ValidationRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Abstract class for input
 * @param <T> the type of the input value
 */
public abstract class Input<T> {
    private String label = "";
    private boolean retryOnInvalid = true;
    private String errorMessage = TextFormatter.format("&cInvalid input. Please try again.");
    private final List<ValidationRule<T>> validationRules;
    private final Scanner scanner = new Scanner(System.in);

    public Input() {
        validationRules = new ArrayList<>();
    }

    /**
     * Read the input value
     * @return the input value
     * @throws InputValidationException if the input is invalid and retryOnInvalid is false
     */
    public abstract T read() throws InputValidationException;

    /**
     * Set the label for the input
     * @param label the label to set (formatted with TextFormatter)
     * @return the input object
     */
    public Input<T> setLabel(String label) {
        this.label = TextFormatter.format(label);
        return this;
    }

    /**
     * Set whether to retry on invalid input
     * @param retryOnInvalid whether to retry on invalid input
     * @return the input object
     */
    public Input<T> setRetryOnInvalid(boolean retryOnInvalid) {
        this.retryOnInvalid = retryOnInvalid;
        return this;
    }

    /**
     * Set the error message for invalid input
     * @param errorMessage the error message to set (formatted with TextFormatter)
     * @return the input object
     */
    public Input<T> setErrorMessage(String errorMessage) {
        this.errorMessage = TextFormatter.format(errorMessage);
        return this;
    }

    /**
     * Add validation rules to the input
     * @param rule the validation rules to add
     * @return the input object
     */
    public Input<T> addValidationRules(ValidationRule<T>... rule) {
        validationRules.addAll(Arrays.asList(rule));
        return this;
    }

    /**
     * Add validation rules to the input
     * @param rules the validation rules to add
     * @return the input object
     */
    public Input<T> addValidationRules(List<ValidationRule<T>> rules) {
        validationRules.addAll(rules);
        return this;
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
                    Printer.println(rule.getErrorMessage());
                    throw new RetryInputException(rule.getErrorMessage());
                } else {
                    throw new InputValidationException(rule.getErrorMessage());
                }
            }
        }
    }

    public String getLabel() {
        return label;
    }

    public boolean isRetryOnInvalid() {
        return retryOnInvalid;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    protected Scanner getScanner() {
        return scanner;
    }
}