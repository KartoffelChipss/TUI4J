package org.strassburger.tui4j.input;

import org.strassburger.tui4j.console.Console;

public class MultilineTextInput extends Input<String, MultilineTextInput> {

    @Override
    protected void printPrompt(Console console) {
        if (getPrompt() != null) console.println(getPrompt());
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
