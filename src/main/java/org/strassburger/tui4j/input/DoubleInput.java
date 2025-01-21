package org.strassburger.tui4j.input;

import org.strassburger.tui4j.formatting.Printer;
import org.strassburger.tui4j.input.exceptions.InputValidationException;
import org.strassburger.tui4j.input.exceptions.RetryInputException;
import org.strassburger.tui4j.input.validationrules.ValidationRule;

import java.util.List;

public class DoubleInput extends Input<Double> {
    public DoubleInput() {
        super();
    }

    public Double read() throws InputValidationException {
        Printer.println(getLabel());
        String input = getScanner().nextLine();

        try {
            double value = Double.parseDouble(input);
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

    public DoubleInput setLabel(String label) {
        super.setLabel(label);
        return this;
    }

    public DoubleInput setRetryOnInvalid(boolean retryOnInvalid) {
        super.setRetryOnInvalid(retryOnInvalid);
        return this;
    }

    @SafeVarargs
    @Override
    public final DoubleInput addValidationRules(ValidationRule<Double>... rules) {
        super.addValidationRules(rules);
        return this;
    }

    @Override
    public DoubleInput addValidationRules(List<ValidationRule<Double>> rules) {
        super.addValidationRules(rules);
        return this;
    }

    @Override
    public DoubleInput setErrorMessage(String errorMessage) {
        super.setErrorMessage(errorMessage);
        return this;
    }
}
