package org.strassburger.tui4j.input;

import org.strassburger.tui4j.formatting.StyledText;
import org.strassburger.tui4j.formatting.ansi.AnsiColor;
import org.strassburger.tui4j.input.exceptions.InputValidationException;
import org.strassburger.tui4j.input.exceptions.RetryInputException;
import org.strassburger.tui4j.printer.Printer;

public class IntegerInput extends Input<Integer, IntegerInput> {
    private boolean inline = true;

    /**
     * @deprecated Use {@link #IntegerInput(Printer)} instead
     */
    public IntegerInput() {
        super();
    }

    public IntegerInput(Printer printer) {
        super(printer);
    }

    public Integer read() throws InputValidationException {
        printPromptAndCursor(inline);

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
