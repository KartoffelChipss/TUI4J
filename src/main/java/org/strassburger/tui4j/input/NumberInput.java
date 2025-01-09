package org.strassburger.tui4j.input;

import org.strassburger.tui4j.input.exceptions.InputValidationException;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class NumberInput<U extends Number> extends Input<U> {
    private static final Map<Class<? extends Number>, Function<String, ? extends Number>> PARSERS = new HashMap<>();
    private final Class<U> type;

    static {
        PARSERS.put(Double.class, Double::valueOf);
        PARSERS.put(Float.class, Float::valueOf);
        PARSERS.put(Integer.class, Integer::valueOf);
        PARSERS.put(Long.class, Long::valueOf);
        PARSERS.put(Short.class, Short::valueOf);
        PARSERS.put(Byte.class, Byte::valueOf);
    }

    public NumberInput(Class<U> type) {
        super();
        this.type = type;
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

    @Override
    public U read() throws InputValidationException {
        System.out.print(label);
        String input = getScanner().nextLine();

        System.out.println("getTypeClass(): " + getTypeClass());

        Function<String, ? extends Number> parser = PARSERS.get(getTypeClass());

        if (parser == null) {
            throw new UnsupportedOperationException("Unsupported number type: " + getTypeClass().getName());
        }

        try {
            return (U) parser.apply(input);
        } catch (NumberFormatException e) {
            if (retryOnInvalid) {
                System.out.println("Invalid input. Please try again.");
                return read();
            } else {
                throw new InputValidationException("Invalid input: " + input);
            }
        }
    }

    private Class<U> getTypeClass() {
        return type;
    }
}