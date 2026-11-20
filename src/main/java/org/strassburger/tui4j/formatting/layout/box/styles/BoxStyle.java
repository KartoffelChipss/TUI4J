package org.strassburger.tui4j.formatting.layout.box.styles;

import org.strassburger.colorlab4j.color.Color;

/**
 * Interface defining the style of a box, including border characters and padding.
 */
public interface BoxStyle {
    /** Returns the color used for the box border */
    Color borderColor();

    /**
     * Returns a copy of this style using the given border color
     * @param color the border color
     * @return a new BoxStyle with the given border color
     */
    BoxStyle withBorderColor(Color color);

    /** Returns the character used for the left vertical border */
    char verticalLeft();
    /** Returns the character used for the right vertical border */
    char verticalRight();

    /** Returns the character used for the top horizontal border */
    char horizontalTop();
    /** Returns the character used for the bottom horizontal border */
    char horizontalBottom();

    // Corners
    char topLeft();
    char topRight();
    char bottomLeft();
    char bottomRight();

    // Layout
    int paddingLeft();
    int paddingRight();
    int paddingTop();
    int paddingBottom();
}
