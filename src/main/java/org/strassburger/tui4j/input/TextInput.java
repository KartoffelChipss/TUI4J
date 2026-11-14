package org.strassburger.tui4j.input;

import org.strassburger.tui4j.console.Console;
import org.strassburger.tui4j.formatting.StyledText;

/**
 * <p>
 * Reads a single line of text from the console.
 * </p>
 * <p>
 * Example usage:
 * </p>
 * <pre>{@code
 * String name = console.read(TextInput.withPrompt("What is your name?")
 *        .retryOnInvalid(true));
 * }</pre>
 */
public class TextInput extends Input<String, TextInput> {

    /**
     * Create a new TextInput with the given prompt
     * @param prompt the prompt to display
     * @return the new TextInput
     */
    public static TextInput withPrompt(String prompt) {
        return new TextInput().prompt(prompt);
    }

    /**
     * Create a new TextInput with the given prompt
     * @param prompt the prompt to display
     * @return the new TextInput
     */
    public static TextInput withPrompt(StyledText prompt) {
        return new TextInput().prompt(prompt);
    }

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
