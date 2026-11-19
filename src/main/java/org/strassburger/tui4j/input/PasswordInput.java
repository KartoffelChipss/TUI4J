package org.strassburger.tui4j.input;

import org.jspecify.annotations.Nullable;
import org.strassburger.tui4j.console.Console;
import org.strassburger.tui4j.formatting.StyledText;

/**
 * <p>
 * Reads a single line of text without showing it, for passwords and other secrets.
 * By default, a {@code *} is shown for every typed character; use {@link #mask(char)} to
 * show another character or {@link #hidden()} to show nothing at all.
 * </p>
 * <p>
 * Example usage:
 * </p>
 * <pre>{@code
 * String password = console.read(PasswordInput.withPrompt("Password")
 *         .validationRule(TextValidationRules.minLength(8)));
 *
 * String pin = console.read(PasswordInput.withPrompt("PIN").hidden());
 * }</pre>
 * <p>
 * How the input is hidden depends on the console; see {@link Console#readPassword(Character)}.
 * The value is returned as a {@link String}, which cannot be wiped from memory after use.
 * </p>
 */
public class PasswordInput extends Input<String, PasswordInput> {

    /**
     * Create a new PasswordInput with the given prompt
     * @param prompt the prompt to display
     * @return the new PasswordInput
     */
    public static PasswordInput withPrompt(String prompt) {
        return new PasswordInput().prompt(prompt);
    }

    /**
     * Create a new PasswordInput with the given prompt
     * @param prompt the prompt to display
     * @return the new PasswordInput
     */
    public static PasswordInput withPrompt(StyledText prompt) {
        return new PasswordInput().prompt(prompt);
    }

    private @Nullable Character mask = '*';
    private boolean inline;

    @Override
    protected void printPrompt(Console console) {
        printPromptAndCursor(console, inline);
    }

    @Override
    protected String readInput(Console console) {
        String value = console.readPassword(mask);
        if (defaultValue() != null) return value;
        while (value.isEmpty()) {
            value = console.readPassword(mask);
        }
        return value;
    }

    @Override
    protected String parse(String input) {
        return input;
    }

    /**
     * Show the given character for every typed character.
     * @param mask the character to show
     * @return the input object
     */
    public PasswordInput mask(char mask) {
        this.mask = mask;
        return this;
    }

    /**
     * Show nothing while typing.
     * @return the input object
     */
    public PasswordInput hidden() {
        this.mask = null;
        return this;
    }

    /**
     * @return the character shown for every typed character, or {@code null} if nothing is shown
     */
    public @Nullable Character mask() {
        return mask;
    }

    /**
     * Set whether the input should be inline or not.
     * @param inline Whether the input should be inline or not
     * @return the input object
     */
    public PasswordInput inline(boolean inline) {
        this.inline = inline;
        return this;
    }

    public boolean inline() {
        return inline;
    }
}
