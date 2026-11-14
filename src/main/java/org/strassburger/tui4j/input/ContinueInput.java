package org.strassburger.tui4j.input;

import org.strassburger.tui4j.console.Console;

public class ContinueInput extends Input<Void, ContinueInput> {
    @Override
    @SuppressWarnings("NullAway") // Void can only ever be null
    public Void read(Console console) {
        if (prompt() != null) console.println(prompt());
        console.readLine();
        return null;
    }

    @Override
    @SuppressWarnings("NullAway") // Void can only ever be null
    protected Void parse(String input) {
        return null;
    }
}
