package org.strassburger.input;

import org.strassburger.input.exceptions.InputValidationException;

public class BooleanInput extends Input<Boolean> {
    public BooleanInput() {
        super();
    }

    public BooleanInput setLabel(String label) {
        super.setLabel(label);
        return this;
    }

    public BooleanInput setRetryOnInvalid(boolean retryOnInvalid) {
        super.setRetryOnInvalid(retryOnInvalid);
        return this;
    }

    public Boolean read() throws InputValidationException {
        System.out.print(label);
        String input = getScanner().nextLine();

        if (input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes")) {
            return true;
        }

        if (input.equalsIgnoreCase("n") || input.equalsIgnoreCase("no")) {
            return false;
        }

        if (retryOnInvalid) {
            System.out.println("Invalid input. Please try again.");
            return read();
        } else {
            throw new InputValidationException("Invalid input.");
        }
    }
}
