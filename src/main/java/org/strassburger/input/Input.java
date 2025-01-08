package org.strassburger.input;

import org.strassburger.formatting.TextFormatter;
import org.strassburger.input.exceptions.InputValidationException;
import org.strassburger.input.exceptions.RetryInputException;
import org.strassburger.input.validationrules.ValidationRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Abstract class for input
 * @param <T> the type of the input value
 */
public abstract class Input<T> {
    protected String label = "";
    protected boolean retryOnInvalid = true;
    protected final List<ValidationRule<T>> validationRules;
    protected Scanner scanner = new Scanner(System.in);

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
     * Add validation rules to the input
     * @param rule the validation rules to add
     * @return the input object
     */
    public Input<T> addValidationRules(ValidationRule<T>... rule) {
        validationRules.addAll(Arrays.asList(rule));
        return this;
    }

    protected void validate(T value) throws InputValidationException {
        for (ValidationRule<T> rule : validationRules) {
            if (!rule.validate(value)) {
                if (retryOnInvalid) {
                    System.out.println(rule.getErrorMessage());
                    throw new RetryInputException(rule.getErrorMessage());
                } else {
                    throw new InputValidationException(rule.getErrorMessage());
                }
            }
        }
    }

    protected Scanner getScanner() {
        return scanner;
    }
}