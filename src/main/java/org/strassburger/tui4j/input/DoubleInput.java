package org.strassburger.tui4j.input;

import org.strassburger.tui4j.console.Console;
import org.strassburger.tui4j.input.exceptions.InputValidationException;

public class DoubleInput extends Input<Double, DoubleInput> {
    private boolean inline = true;
    private boolean allowComma = true;

    @Override
    protected void printPrompt(Console console) {
        printPromptAndCursor(console, inline);
    }

    @Override
    protected Double parse(String input) throws InputValidationException {
        if (allowComma) input = input.replace(",", ".");
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            throw invalidInput();
        }
    }

    public DoubleInput setInline(boolean inline) {
        this.inline = inline;
        return this;
    }

    /**
     * Set whether the input should allow commas as decimal separators.
     * @param allowComma Whether to allow commas as decimal separators.
     * @return The DoubleInput object.
     */
    public DoubleInput setAllowComma(boolean allowComma) {
        this.allowComma = allowComma;
        return this;
    }
}
