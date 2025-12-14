package org.strassburger.tui4j.input;

import org.strassburger.tui4j.input.exceptions.InputValidationException;
import org.strassburger.tui4j.input.exceptions.RetryInputException;
import org.strassburger.tui4j.printer.Printer;

public class TextInput extends Input<String, TextInput> {
    private boolean inline;

    /**
     * <p>
     * Reads a single line of text from the console.
     * </p>
     * <p>
     * Example usage:
     * </p>
     * <pre>{@code
     * String name = new TextInput()
     *        .setLabel("What is your name?")
     *        .setRetryOnInvalid(true)
     *        .read();
     * }</pre>
     * @deprecated Use {@link #TextInput(Printer)} instead
     */
    public TextInput() {
        super();
    }

    /**
     * <p>
     * Reads a single line of text from the console.
     * </p>
     * <p>
     * Example usage:
     * </p>
     * <pre>{@code
     * String name = new TextInput()
     *        .setLabel("What is your name?")
     *        .setRetryOnInvalid(true)
     *        .read();
     * }</pre>
     */
    public TextInput(Printer printer) {
        super(printer);
    }

    @Override
    public String read() throws InputValidationException {
        printPromptAndCursor(inline);

        String value = getScanner().nextLine();
        while (value.isEmpty()) {
            value = getScanner().nextLine();
        }

        try {
            validate(value);
        } catch (RetryInputException e) {
            return read();
        }

        return value;
    }

    /**
     * Set whether the input should be inline or not.
     * @param inline Whether the input should be inline or not
     */
    public TextInput setInline(boolean inline) {
        this.inline = inline;
        return this;
    }
}