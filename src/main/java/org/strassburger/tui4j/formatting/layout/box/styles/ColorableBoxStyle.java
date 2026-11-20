package org.strassburger.tui4j.formatting.layout.box.styles;

import org.strassburger.colorlab4j.color.Color;
import org.strassburger.tui4j.formatting.ansi.AnsiColor;

/**
 * Base class for box styles with a configurable border color.
 * {@link #withBorderColor(Color)} returns a copy, so shared presets in {@link BoxStyles} are never modified.
 */
public abstract class ColorableBoxStyle implements BoxStyle, Cloneable {
    private Color borderColor = AnsiColor.DEFAULT;

    @Override
    public Color borderColor() {
        return borderColor;
    }

    @Override
    public BoxStyle withBorderColor(Color color) {
        try {
            ColorableBoxStyle copy = (ColorableBoxStyle) clone();
            copy.borderColor = color;
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public int paddingLeft() { return 1; }
    @Override
    public int paddingRight() { return 1; }
    @Override
    public int paddingTop() { return 0; }
    @Override
    public int paddingBottom() { return 0; }
}
