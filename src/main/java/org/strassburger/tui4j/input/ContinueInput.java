package org.strassburger.tui4j.input;

public class ContinueInput extends Input<Void, ContinueInput> {
    public ContinueInput() {
        super();
    }

    @Override
    public Void read() {
        if (getPrompt() != null) getPrinter().println(getPrompt());
        getScanner().nextLine();
        return null;
    }
}
