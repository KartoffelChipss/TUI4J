package org.strassburger.tui4j.input;

import org.strassburger.tui4j.formatting.TextColor;
import org.strassburger.tui4j.formatting.TextFormatter;
import org.strassburger.tui4j.input.exceptions.InputValidationException;
import org.strassburger.tui4j.input.exceptions.RetryInputException;
import org.strassburger.tui4j.input.validationrules.ValidationRule;

public class TextInput extends Input<String> {
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
     */
    public TextInput() {
        super();
    }

    @Override
    public String read() throws InputValidationException {
        System.out.print(label);

        if (inline) System.out.print("");
        else {
            System.out.println();
            System.out.print(TextFormatter.format("&8> "));
        }

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

    @Override
    public TextInput setLabel(String label) {
        super.setLabel(label);
        return this;
    }

    @Override
    public TextInput setRetryOnInvalid(boolean retryOnInvalid) {
        super.setRetryOnInvalid(retryOnInvalid);
        return this;
    }

    @SafeVarargs
    @Override
    public final TextInput addValidationRules(ValidationRule<String>... rules) {
        super.addValidationRules(rules);
        return this;
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