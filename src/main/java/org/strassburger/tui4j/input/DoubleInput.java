package org.strassburger.tui4j.input;

import org.strassburger.tui4j.formatting.Printer;
import org.strassburger.tui4j.input.exceptions.InputValidationException;
import org.strassburger.tui4j.input.exceptions.RetryInputException;
import org.strassburger.tui4j.input.validationrules.ValidationRule;

import java.util.List;

public class DoubleInput extends Input<Double> {
    private boolean inline = true;
    private boolean allowComma = true;

    public DoubleInput() {
        super();
    }

    public Double read() throws InputValidationException {
        Printer.print(getLabel());

        if (inline) System.out.print("");
        else {
            System.out.println();
            Printer.print("&8> ");
        }

        String input = getScanner().nextLine();
        if (allowComma) input = input.replace(",", ".");

        try {
            double value = Double.parseDouble(input);
            validate(value);
            return value;
        } catch (NumberFormatException e) {
            if (isRetryOnInvalid()) {
                Printer.println(getErrorMessage());
                return read();
            } else {
                throw new InputValidationException("Invalid input: " + input);
            }
        } catch (RetryInputException e) {
            return read();
        }
    }

    public DoubleInput setInline(boolean inline) {
        this.inline = inline;
        return this;
    }

    public DoubleInput setLabel(String label) {
        super.setLabel(label);
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

    public DoubleInput setRetryOnInvalid(boolean retryOnInvalid) {
        super.setRetryOnInvalid(retryOnInvalid);
        return this;
    }

    @SafeVarargs
    @Override
    public final DoubleInput addValidationRules(ValidationRule<Double>... rules) {
        super.addValidationRules(rules);
        return this;
    }

    @Override
    public DoubleInput addValidationRules(List<ValidationRule<Double>> rules) {
        super.addValidationRules(rules);
        return this;
    }

    @Override
    public DoubleInput setErrorMessage(String errorMessage) {
        super.setErrorMessage(errorMessage);
        return this;
    }
}
