package org.strassburger.tui4j.input;

import org.strassburger.tui4j.console.Console;
import org.strassburger.tui4j.formatting.StyledText;
import org.strassburger.tui4j.input.exceptions.InputValidationException;

public class IntegerInput extends Input<Integer, IntegerInput> {

    /**
     * Create a new IntegerInput with the given prompt
     * @param prompt the prompt to display
     * @return the new IntegerInput
     */
    public static IntegerInput withPrompt(String prompt) {
        return new IntegerInput().prompt(prompt);
    }

    /**
     * Create a new IntegerInput with the given prompt
     * @param prompt the prompt to display
     * @return the new IntegerInput
     */
    public static IntegerInput withPrompt(StyledText prompt) {
        return new IntegerInput().prompt(prompt);
    }

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

    public IntegerInput inline(boolean inline) {
        this.inline = inline;
        return this;
    }

    public boolean inline() {
        return inline;
    }
}
