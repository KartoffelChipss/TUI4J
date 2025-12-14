package org.strassburger.tui4j.input;

import org.strassburger.tui4j.formatting.StyledText;
import org.strassburger.tui4j.formatting.ansi.AnsiColor;
import org.strassburger.tui4j.input.exceptions.InputValidationException;
import org.strassburger.tui4j.printer.Printer;

public class BooleanInput extends Input<Boolean, BooleanInput> {

    /**
     * @deprecated Use {@link #BooleanInput(Printer)} instead
     */
    public BooleanInput() {
        super();
    }

    public BooleanInput(Printer printer) {
        super(printer);
    }

    public Boolean read() throws InputValidationException {
        if (getPrompt() != null) getPrinter().print(getPrompt());
        String input = getScanner().nextLine();

        if (input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes")) {
            return true;
        }

        if (input.equalsIgnoreCase("n") || input.equalsIgnoreCase("no")) {
            return false;
        }

        if (isRetryOnInvalid()) {
            getPrinter().println(getErrorMessage());
            return read();
        } else {
            throw new InputValidationException(StyledText.text("Invalid input.").fg(AnsiColor.RED));
        }
    }
}
