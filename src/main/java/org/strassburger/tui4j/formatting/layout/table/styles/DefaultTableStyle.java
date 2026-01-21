package org.strassburger.tui4j.formatting.layout.table.styles;

import org.strassburger.colorlab4j.color.Color;

/**
 * A TableStyle implementation that mimics the style of MySQL command line ASCII tables.
 */
public class DefaultTableStyle extends ColorableTableStyle {
    public char verticalLeft() { return vertical(); }
    public char verticalRight() { return vertical(); }
    public char vertical() { return '|'; }

    public char horizontalTop() { return horizontal(); }
    public char horizontalBottom() { return horizontal(); }
    public char horizontalHeaderSeparator() { return horizontal(); }
    public char horizontal() { return '-'; }

    public char topLeft() { return '+'; }
    public char topRight() { return '+'; }
    public char bottomLeft() { return '+'; }
    public char bottomRight() { return '+'; }

    public char teeTop() { return '+'; }
    public char teeBottom() { return '+'; }
    public char teeLeft() { return '+'; }
    public char teeRight() { return '+'; }

    public char intersection() { return '+'; }

    public int paddingLeft() { return 1; }
    public int paddingRight() { return 1; }
}
