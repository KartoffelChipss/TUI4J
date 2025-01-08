package org.strassburger.input;

public class ContinueInput extends Input<Void> {
    public ContinueInput() {
        super();
    }

    @Override
    public ContinueInput setLabel(String label) {
        super.setLabel(label);
        return this;
    }

    @Override
    public Void read() {
        System.out.print(label);
        getScanner().nextLine();
        return null;
    }
}
