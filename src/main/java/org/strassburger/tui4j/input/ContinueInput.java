package org.strassburger.tui4j.input;

import org.strassburger.tui4j.printer.Printer;

public class ContinueInput extends Input<Void, ContinueInput> {
    public ContinueInput(Printer printer) {
        super(printer);
    }

    @Override
    public Void read() {
        if (getPrompt() != null) getPrinter().println(getPrompt());
        getScanner().nextLine();
        return null;
    }
}
