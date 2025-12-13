package org.strassburger.tui4j.input;

import org.strassburger.tui4j.input.exceptions.InputValidationException;
import org.strassburger.tui4j.input.exceptions.RetryInputException;

import java.util.List;

public class MultilineTextInput extends Input<String, MultilineTextInput> {
    private boolean inline;

    public MultilineTextInput() {
        super();
    }

    @Override
    public String read() throws InputValidationException {
        StringBuilder input = new StringBuilder();
        String line;
        getPrinter().println(getLabel());

        if (inline) getPrinter().print(" ");
        else getPrinter().println();

        // Read input line by line until the user presses Enter (on a blank line, finish)
        while (true) {
            line = getScanner().nextLine();
            if (line.isEmpty()) {
                break;
            }
            input.append(line).append("\n");
        }

        try {
            validate(input.toString().trim());
        } catch (RetryInputException e) {
            return read();
        }

        return input.toString().trim();
    }

    public MultilineTextInput setInline(boolean inline) {
        this.inline = inline;
        return this;
    }
}