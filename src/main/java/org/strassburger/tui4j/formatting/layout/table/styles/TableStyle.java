package org.strassburger.tui4j.formatting.layout.table.styles;

import org.strassburger.colorlab4j.color.Color;

/**
 * Interface defining the style of a table, including border characters and layout options.
 */
public interface TableStyle {
    /** Returns the color used for the table borders */
    Color borderColor();
    TableStyle withBorderColor(Color color);

    /** Returns the character used for the left vertical border */
    char verticalLeft();

    /** Returns the character used for the right vertical border */
    char verticalRight();
    /** Returns the character used for vertical borders between columns */
    char vertical();

    /** Returns the character used for the top horizontal border */
    char horizontalTop();
    /** Returns the character used for the bottom horizontal border */
    char horizontalBottom();
    /** Returns the character used for horizontal border below the header if present */
    char horizontalHeaderSeparator();
    /** Returns the character used for horizontal borders between rows */
    char horizontal();

    // Corners
    char topLeft();
    char topRight();
    char bottomLeft();
    char bottomRight();

    char teeTop(); // (vertical down, horizontal left/right)
    char teeBottom(); // (vertical up, horizontal left/right)
    char teeLeft(); // (horizontal right, vertical up/down)
    char teeRight(); // (horizontal left, vertical up/down)

    char intersection();

    // Layout
    int paddingLeft();
    int paddingRight();
}
