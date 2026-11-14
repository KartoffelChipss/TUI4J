package org.strassburger.tui4j.input;

import org.strassburger.tui4j.console.Console;

/**
 * <p>
 * Reads a single line of text from the console.
 * </p>
 * <p>
 * Example usage:
 * </p>
 * <pre>{@code
 * String name = console.read(new TextInput()
 *        .prompt("What is your name?")
 *        .retryOnInvalid(true));
 * }</pre>
 */
public class TextInput extends Input<String, TextInput> {
    private boolean inline;

    @Override
    protected void printPrompt(Console console) {
        printPromptAndCursor(console, inline);
    }

    @Override
    protected String readInput(Console console) {
        String value = console.readLine();
        while (value.isEmpty()) {
            value = console.readLine();
        }
        return value;
    }

    @Override
    protected String parse(String input) {
        return input;
    }

    /**
     * Set whether the input should be inline or not.
     * @param inline Whether the input should be inline or not
     */
    public TextInput inline(boolean inline) {
        this.inline = inline;
        return this;
    }

    public boolean inline() {
        return inline;
    }
}
