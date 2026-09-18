package org.strassburger.tui4j.formatting.layout.table.styles;

import org.strassburger.colorlab4j.color.Color;

/**
 * A TableStyle implementation that mimics the style of MySQL command line ASCII tables.
 */
public class DefaultTableStyle extends ColorableTableStyle {
    @Override
    public char verticalLeft() { return vertical(); }
    @Override
    public char verticalRight() { return vertical(); }
    @Override
    public char vertical() { return '|'; }

    @Override
    public char horizontalTop() { return horizontal(); }
    @Override
    public char horizontalBottom() { return horizontal(); }
    @Override
    public char horizontalHeaderSeparator() { return horizontal(); }
    @Override
    public char horizontal() { return '-'; }

    @Override
    public char topLeft() { return '+'; }
    @Override
    public char topRight() { return '+'; }
    @Override
    public char bottomLeft() { return '+'; }
    @Override
    public char bottomRight() { return '+'; }

    @Override
    public char teeTop() { return '+'; }
    @Override
    public char teeBottom() { return '+'; }
    @Override
    public char teeLeft() { return '+'; }
    @Override
    public char teeRight() { return '+'; }

    @Override
    public char intersection() { return '+'; }

    @Override
    public int paddingLeft() { return 1; }
    @Override
    public int paddingRight() { return 1; }
}
