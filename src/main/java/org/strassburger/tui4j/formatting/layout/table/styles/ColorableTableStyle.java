package org.strassburger.tui4j.formatting.layout.table.styles;

import org.strassburger.colorlab4j.color.Color;
import org.strassburger.tui4j.formatting.ansi.AnsiColor;

public abstract class ColorableTableStyle implements TableStyle {
    private Color borderColor = AnsiColor.DEFAULT;

    @Override
    public Color borderColor() {
        return borderColor;
    }

    @Override
    public TableStyle withBorderColor(Color color) {
        this.borderColor = color;
        return this;
    }
}
