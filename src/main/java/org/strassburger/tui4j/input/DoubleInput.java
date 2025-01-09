package org.strassburger.tui4j.input;

import org.strassburger.tui4j.input.exceptions.InputValidationException;
import org.strassburger.tui4j.input.exceptions.RetryInputException;
import org.strassburger.tui4j.input.validationrules.ValidationRule;

public class DoubleInput extends Input<Double> {
    public DoubleInput() {
        super();
    }

    public DoubleInput setLabel(String label) {
        super.setLabel(label);
        return this;
    }

    public DoubleInput setRetryOnInvalid(boolean retryOnInvalid) {
        super.setRetryOnInvalid(retryOnInvalid);
        return this;
    }

    public Double read() throws InputValidationException {
        System.out.print(label);
        String input = getScanner().nextLine();

        try {
            double value = Double.parseDouble(input);
            validate(value);
            return value;
        } catch (NumberFormatException e) {
            if (retryOnInvalid) {
                System.out.println("Invalid input. Please try again.");
                return read();
            } else {
                throw new InputValidationException("Invalid input: " + input);
            }
        } catch (RetryInputException e) {
            return read();
        }
    }

    @SafeVarargs
    @Override
    public final DoubleInput addValidationRules(ValidationRule<Double>... rules) {
        super.addValidationRules(rules);
        return this;
    }
}
