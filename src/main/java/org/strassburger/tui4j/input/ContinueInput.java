package org.strassburger.tui4j.input;

public class ContinueInput extends Input<Void, ContinueInput> {
    public ContinueInput() {
        super();
    }

    @Override
    public Void read() {
        if (getLabel() != null) getPrinter().println(getLabel());
        getScanner().nextLine();
        return null;
    }
}
