package org.strassburger.tui4j.console;

import org.strassburger.tui4j.input.Input;
import org.strassburger.tui4j.input.exceptions.InputValidationException;

public interface Console extends Printer {
    String readLine();

    default <T> T read(Input<T, ?> input) throws InputValidationException {
        return input.read(this);
    }
}
