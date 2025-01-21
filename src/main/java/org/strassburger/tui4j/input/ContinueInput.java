package org.strassburger.tui4j.input;

import org.strassburger.tui4j.formatting.Printer;

public class ContinueInput extends Input<Void> {
    public ContinueInput() {
        super();
    }

    @Override
    public Void read() {
        Printer.println(getLabel());
        getScanner().nextLine();
        return null;
    }

    @Override
    public ContinueInput setLabel(String label) {
        super.setLabel(label);
        return this;
    }
}
