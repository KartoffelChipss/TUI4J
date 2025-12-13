package org.strassburger.tui4j.input;

import org.strassburger.tui4j.formatting.StyledText;
import org.strassburger.tui4j.formatting.ansi.AnsiColor;
import org.strassburger.tui4j.input.exceptions.InputValidationException;
import org.strassburger.tui4j.input.exceptions.RetryInputException;

public class DoubleInput extends Input<Double, DoubleInput> {
    private boolean inline = true;
    private boolean allowComma = true;

    public DoubleInput() {
        super();
    }

    public Double read() throws InputValidationException {
        getPrinter().print(getLabel());

        if (inline) getPrinter().print("");
        else {
            getPrinter().println();
            getPrinter().print(StyledText.text("> ").fg(AnsiColor.BRIGHT_BLACK));
        }

        String input = getScanner().nextLine();
        if (allowComma) input = input.replace(",", ".");

        try {
            double value = Double.parseDouble(input);
            validate(value);
            return value;
        } catch (NumberFormatException e) {
            if (isRetryOnInvalid()) {
                getPrinter().println(getErrorMessage());
                return read();
            } else {
                throw new InputValidationException(StyledText.text("Invalid input: " + input).fg(AnsiColor.RED));
            }
        } catch (RetryInputException e) {
            return read();
        }
    }

    public DoubleInput setInline(boolean inline) {
        this.inline = inline;
        return this;
    }

    /**
     * Set whether the input should allow commas as decimal separators.
     * @param allowComma Whether to allow commas as decimal separators.
     * @return The DoubleInput object.
     */
    public DoubleInput setAllowComma(boolean allowComma) {
        this.allowComma = allowComma;
        return this;
    }
}
