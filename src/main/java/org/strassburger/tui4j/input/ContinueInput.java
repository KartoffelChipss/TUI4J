package org.strassburger.tui4j.input;

import org.strassburger.tui4j.console.Console;
import org.strassburger.tui4j.formatting.StyledText;

public class ContinueInput extends Input<Void, ContinueInput> {

    /**
     * Create a new ContinueInput with the given prompt
     * @param prompt the prompt to display
     * @return the new ContinueInput
     */
    public static ContinueInput withPrompt(String prompt) {
        return new ContinueInput().prompt(prompt);
    }

    /**
     * Create a new ContinueInput with the given prompt
     * @param prompt the prompt to display
     * @return the new ContinueInput
     */
    public static ContinueInput withPrompt(StyledText prompt) {
        return new ContinueInput().prompt(prompt);
    }

    @Override
    @SuppressWarnings("NullAway") // Void can only ever be null
    public Void read(Console console) {
        if (prompt() != null) console.println(prompt());
        console.readLine();
        return null;
    }

    @Override
    @SuppressWarnings("NullAway") // Void can only ever be null
    protected Void parse(String input) {
        return null;
    }
}
