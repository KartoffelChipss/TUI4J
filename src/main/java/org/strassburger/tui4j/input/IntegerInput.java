package org.strassburger.tui4j.input;

import org.strassburger.tui4j.console.Console;
import org.strassburger.tui4j.input.exceptions.InputValidationException;

public class IntegerInput extends Input<Integer, IntegerInput> {
    private boolean inline = true;

    @Override
    protected void printPrompt(Console console) {
        printPromptAndCursor(console, inline);
    }

    @Override
    protected Integer parse(String input) throws InputValidationException {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw invalidInput();
        }
    }

    public IntegerInput setInline(boolean inline) {
        this.inline = inline;
        return this;
    }
}
