package org.strassburger.tui4j.input;

import org.strassburger.tui4j.console.Console;
import org.strassburger.tui4j.input.exceptions.InputValidationException;

public class BooleanInput extends Input<Boolean, BooleanInput> {

    @Override
    protected void printPrompt(Console console) {
        if (getPrompt() != null) console.print(getPrompt());
    }

    @Override
    protected Boolean parse(String input) throws InputValidationException {
        if (input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes")) return true;
        if (input.equalsIgnoreCase("n") || input.equalsIgnoreCase("no")) return false;
        throw invalidInput();
    }
}
