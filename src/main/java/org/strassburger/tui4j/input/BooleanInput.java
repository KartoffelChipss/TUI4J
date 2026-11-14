package org.strassburger.tui4j.input;

import org.strassburger.tui4j.console.Console;
import org.strassburger.tui4j.formatting.StyledText;
import org.strassburger.tui4j.input.exceptions.InputValidationException;

public class BooleanInput extends Input<Boolean, BooleanInput> {

    /**
     * Create a new BooleanInput with the given prompt
     * @param prompt the prompt to display
     * @return the new BooleanInput
     */
    public static BooleanInput withPrompt(String prompt) {
        return new BooleanInput().prompt(prompt);
    }

    /**
     * Create a new BooleanInput with the given prompt
     * @param prompt the prompt to display
     * @return the new BooleanInput
     */
    public static BooleanInput withPrompt(StyledText prompt) {
        return new BooleanInput().prompt(prompt);
    }

    @Override
    protected void printPrompt(Console console) {
        if (prompt() != null) console.print(prompt());
    }

    @Override
    protected Boolean parse(String input) throws InputValidationException {
        if (input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes")) return true;
        if (input.equalsIgnoreCase("n") || input.equalsIgnoreCase("no")) return false;
        throw invalidInput();
    }
}
