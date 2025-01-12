package org.strassburger.tui4j.input;

import org.strassburger.tui4j.formatting.Printer;
import org.strassburger.tui4j.input.exceptions.InputValidationException;
import org.strassburger.tui4j.input.exceptions.RetryInputException;
import org.strassburger.tui4j.input.validationrules.ValidationRule;

import java.util.List;

public class IntegerInput extends Input<Integer> {
    public IntegerInput() {
        super();
    }

    public Integer read() throws InputValidationException {
        System.out.print(getLabel());
        String input = getScanner().nextLine();

        try {
            int value = Integer.parseInt(input);
            validate(value);
            return value;
        } catch (NumberFormatException e) {
            if (isRetryOnInvalid()) {
                Printer.println(getErrorMessage());
                return read();
            } else {
                throw new InputValidationException("Invalid input: " + input);
            }
        } catch (RetryInputException e) {
            return read();
        }
    }

    @Override
    public IntegerInput setLabel(String label) {
        super.setLabel(label);
        return this;
    }

    @Override
    public IntegerInput setRetryOnInvalid(boolean retryOnInvalid) {
        super.setRetryOnInvalid(retryOnInvalid);
        return this;
    }

    @SafeVarargs
    @Override
    public final IntegerInput addValidationRules(ValidationRule<Integer>... rules) {
        super.addValidationRules(rules);
        return this;
    }

    @Override
    public IntegerInput addValidationRules(List<ValidationRule<Integer>> rules) {
        super.addValidationRules(rules);
        return this;
    }

    @Override
    public IntegerInput setErrorMessage(String errorMessage) {
        super.setErrorMessage(errorMessage);
        return this;
    }
}
