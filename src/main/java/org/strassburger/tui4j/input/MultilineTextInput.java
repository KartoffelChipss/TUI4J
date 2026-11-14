package org.strassburger.tui4j.input;

import org.strassburger.tui4j.console.Console;
import org.strassburger.tui4j.formatting.StyledText;

public class MultilineTextInput extends Input<String, MultilineTextInput> {

    /**
     * Create a new MultilineTextInput with the given prompt
     * @param prompt the prompt to display
     * @return the new MultilineTextInput
     */
    public static MultilineTextInput withPrompt(String prompt) {
        return new MultilineTextInput().prompt(prompt);
    }

    /**
     * Create a new MultilineTextInput with the given prompt
     * @param prompt the prompt to display
     * @return the new MultilineTextInput
     */
    public static MultilineTextInput withPrompt(StyledText prompt) {
        return new MultilineTextInput().prompt(prompt);
    }

    @Override
    protected void printPrompt(Console console) {
        if (prompt() != null) console.println(prompt());
    }

    /**
     * Reads input line by line until the user enters a blank line
     */
    @Override
    protected String readInput(Console console) {
        StringBuilder input = new StringBuilder();
        String line;
        while (!(line = console.readLine()).isEmpty()) {
            input.append(line).append("\n");
        }
        return input.toString();
    }

    @Override
    protected String parse(String input) {
        return input.trim();
    }
}
