package org.strassburger.tui4j.input;

import org.strassburger.tui4j.input.exceptions.InputValidationException;
import org.strassburger.tui4j.input.exceptions.RetryInputException;
import org.strassburger.tui4j.input.validationrules.ValidationRule;

public class IntegerInput extends Input<Integer> {
    public IntegerInput() {
        super();
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

    public Integer read() throws InputValidationException {
        System.out.print(label);
        String input = getScanner().nextLine();

        try {
            int value = Integer.parseInt(input);
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
    public final IntegerInput addValidationRules(ValidationRule<Integer>... rules) {
        super.addValidationRules(rules);
        return this;
    }
}
