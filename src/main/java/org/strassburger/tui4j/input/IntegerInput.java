package org.strassburger.tui4j.input;

import org.strassburger.tui4j.formatting.StyledText;
import org.strassburger.tui4j.formatting.ansi.AnsiColor;
import org.strassburger.tui4j.input.exceptions.InputValidationException;
import org.strassburger.tui4j.input.exceptions.RetryInputException;

public class IntegerInput extends Input<Integer, IntegerInput> {
    private boolean inline = true;

    public IntegerInput() {
        super();
    }

    public Integer read() throws InputValidationException {
        printLabelAndCursor(inline);

        String input = getScanner().nextLine();

        try {
            int value = Integer.parseInt(input);
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

    public IntegerInput setInline(boolean inline) {
        this.inline = inline;
        return this;
    }
}
