package org.strassburger.tui4j.input;

public class ContinueInput extends Input<Void> {
    public ContinueInput() {
        super();
    }

    @Override
    public Void read() {
        System.out.print(getLabel());
        getScanner().nextLine();
        return null;
    }

    @Override
    public ContinueInput setLabel(String label) {
        super.setLabel(label);
        return this;
    }
}
