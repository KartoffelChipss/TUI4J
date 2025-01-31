package org.strassburger.tui4j.input;

import org.strassburger.tui4j.formatting.Printer;
import org.strassburger.tui4j.input.exceptions.InputValidationException;
import org.strassburger.tui4j.input.validationrules.ValidationRule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * A generic class for handling numeric user input with validation.
 *
 * @param <U> the type of number (Integer, Double, etc.)
 */
public class NumberInput<U extends Number> extends Input<U> {
    private static final Map<Class<? extends Number>, Function<String, ? extends Number>> PARSERS = new HashMap<>();
    private final Class<U> type;
    private boolean allowComma = true;
    private boolean inline = true;

    static {
        PARSERS.put(Double.class, Double::valueOf);
        PARSERS.put(Float.class, Float::valueOf);
        PARSERS.put(Integer.class, Integer::valueOf);
        PARSERS.put(Long.class, Long::valueOf);
        PARSERS.put(Short.class, Short::valueOf);
        PARSERS.put(Byte.class, Byte::valueOf);
    }

    /**
     * Create a new NumberInput object.
     * @param type The type of number to read (Integer, Double, etc.)
     *
     * <p>Example usage:</p>
     * <pre>
     * {@code
     * NumberInput<Integer> input = new NumberInput<>(Integer.class)
     *     .setLabel("Enter an integer:")
     *     .setRetryOnInvalid(true);
     *
     * int value = input.read();
     * System.out.println("You entered: " + value);
     * }
     * </pre>
     */
    public NumberInput(Class<U> type) {
        super();
        this.type = type;
    }

    @Override
    public U read() throws InputValidationException {
        Printer.print(getLabel());

        if (inline) System.out.print("");
        else {
            System.out.println();
            Printer.print("&8> ");
        }

        String input = getScanner().nextLine();
        if (!allowComma) input = input.replace(",", "");

        Function<String, ? extends Number> parser = PARSERS.get(getTypeClass());

        if (parser == null) {
            throw new UnsupportedOperationException("Unsupported number type: " + getTypeClass().getName());
        }

        try {
            return (U) parser.apply(input);
        } catch (NumberFormatException e) {
            if (isRetryOnInvalid()) {
                Printer.println(getErrorMessage());
                return read();
            } else {
                throw new InputValidationException("Invalid input: " + input);
            }
        }
    }

    /**
     * Set whether to allow commas as decimal separators.
     * @param allowComma Whether to allow commas as decimal separators.
     * @return The current NumberInput object.
     */
    public NumberInput<U> setAllowComma(boolean allowComma) {
        this.allowComma = allowComma;
        return this;
    }

    /**
     * Set whether to display the input prompt inline.
     * @param inline Whether to display the input prompt inline.
     * @return The current NumberInput object.
     */
    public NumberInput<U> setInline(boolean inline) {
        this.inline = inline;
        return this;
    }

    @Override
    public NumberInput<U> setLabel(String label) {
        super.setLabel(label);
        return this;
    }

    @Override
    public NumberInput<U> setRetryOnInvalid(boolean retryOnInvalid) {
        super.setRetryOnInvalid(retryOnInvalid);
        return this;
    }

    @SafeVarargs
    @Override
    public final NumberInput<U> addValidationRules(ValidationRule<U>... rules) {
        for (ValidationRule<U> rule : rules) {
            super.addValidationRules(rule);
        }
        return this;
    }

    @Override
    public NumberInput<U> addValidationRules(List<ValidationRule<U>> rules) {
        super.addValidationRules(rules);
        return this;
    }

    private Class<U> getTypeClass() {
        return type;
    }

    @Override
    public NumberInput<U> setErrorMessage(String errorMessage) {
        super.setErrorMessage(errorMessage);
        return this;
    }
}