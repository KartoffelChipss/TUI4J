package org.strassburger.tui4j.formatting.layout.box.styles;

/**
 * Enum representing where the title of a box is placed in its border.
 */
public enum BoxTitlePosition {
    TOP_LEFT,
    TOP_CENTER,
    TOP_RIGHT,
    BOTTOM_LEFT,
    BOTTOM_CENTER,
    BOTTOM_RIGHT;

    /** Returns true if the title is placed in the top border */
    public boolean isTop() {
        return this == TOP_LEFT || this == TOP_CENTER || this == TOP_RIGHT;
    }
}
