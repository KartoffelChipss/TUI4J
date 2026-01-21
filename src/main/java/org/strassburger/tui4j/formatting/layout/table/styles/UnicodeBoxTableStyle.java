package org.strassburger.tui4j.formatting.layout.table.styles;

public class UnicodeBoxTableStyle extends ColorableTableStyle {
    public char verticalLeft() { return '│'; }
    public char verticalRight() { return '│'; }
    public char vertical() { return '│'; }

    public char horizontalTop() { return '─'; }
    public char horizontalBottom() { return '─'; }
    public char horizontalHeaderSeparator() { return '─'; }
    public char horizontal() { return '─'; }

    public char topLeft() { return '┌'; }
    public char topRight() { return '┐'; }
    public char bottomLeft() { return '└'; }
    public char bottomRight() { return '┘'; }

    public char intersection() { return '┼'; }

    public char teeTop() { return '┬'; }
    public char teeBottom() { return '┴'; }
    public char teeLeft() { return '├'; }
    public char teeRight() { return '┤'; }

    public int paddingLeft() { return 1; }
    public int paddingRight() { return 1; }
}
