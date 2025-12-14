package org.strassburger.tui4j.input;

import org.strassburger.tui4j.formatting.StyledText;
import org.strassburger.tui4j.formatting.ansi.AnsiColor;
import org.strassburger.tui4j.input.exceptions.InputValidationException;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * A generic class for handling numeric user input with validation.
 *
 * @param <U> the type of number (Integer, Double, etc.)
 */
public class NumberInput<U extends Number> extends Input<U, NumberInput<U>> {
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
        printPromptAndCursor(inline);

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
                getPrinter().println(getErrorMessage());
                return read();
            } else {
                throw new InputValidationException(StyledText.text("Invalid input: " + input).fg(AnsiColor.RED));
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

    private Class<U> getTypeClass() {
        return type;
    }
}