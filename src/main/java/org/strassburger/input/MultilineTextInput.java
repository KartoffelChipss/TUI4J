package org.strassburger.input;

import org.strassburger.input.exceptions.InputValidationException;
import org.strassburger.input.exceptions.RetryInputException;
import org.strassburger.input.validationrules.ValidationRule;

public class MultilineTextInput extends Input<String> {
    private boolean inline;

    public MultilineTextInput() {
        super();
    }

    @Override
    public String read() throws InputValidationException {
        StringBuilder input = new StringBuilder();
        String line;
        System.out.print(label);

        if (inline) System.out.print(" ");
        else System.out.println();

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

    @Override
    public MultilineTextInput setLabel(String label) {
        super.setLabel(label);
        return this;
    }

    @Override
    public MultilineTextInput setRetryOnInvalid(boolean retryOnInvalid) {
        super.setRetryOnInvalid(retryOnInvalid);
        return this;
    }

    @SafeVarargs
    @Override
    public final MultilineTextInput addValidationRules(ValidationRule<String>... rules) {
        super.addValidationRules(rules);
        return this;
    }

    public MultilineTextInput setInline(boolean inline) {
        this.inline = inline;
        return this;
    }
}