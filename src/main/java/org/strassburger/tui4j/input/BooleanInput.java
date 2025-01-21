package org.strassburger.tui4j.input;

import org.strassburger.tui4j.formatting.Printer;
import org.strassburger.tui4j.input.exceptions.InputValidationException;

public class BooleanInput extends Input<Boolean> {
    public BooleanInput() {
        super();
    }

    public Boolean read() throws InputValidationException {
        Printer.println(getLabel());
        String input = getScanner().nextLine();

        if (input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes")) {
            return true;
        }

        if (input.equalsIgnoreCase("n") || input.equalsIgnoreCase("no")) {
            return false;
        }

        if (isRetryOnInvalid()) {
            Printer.println(getErrorMessage());
            return read();
        } else {
            throw new InputValidationException("Invalid input.");
        }
    }

    public BooleanInput setLabel(String label) {
        super.setLabel(label);
        return this;
    }

    public BooleanInput setRetryOnInvalid(boolean retryOnInvalid) {
        super.setRetryOnInvalid(retryOnInvalid);
        return this;
    }

    @Override
    public BooleanInput setErrorMessage(String errorMessage) {
        super.setErrorMessage(errorMessage);
        return this;
    }
}
