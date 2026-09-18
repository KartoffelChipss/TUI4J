package org.strassburger.tui4j.formatting.layout.table.styles;

public class UnicodeBoxTableStyle extends ColorableTableStyle {
    @Override
    public char verticalLeft() { return '│'; }
    @Override
    public char verticalRight() { return '│'; }
    @Override
    public char vertical() { return '│'; }

    @Override
    public char horizontalTop() { return '─'; }
    @Override
    public char horizontalBottom() { return '─'; }
    @Override
    public char horizontalHeaderSeparator() { return '─'; }
    @Override
    public char horizontal() { return '─'; }

    @Override
    public char topLeft() { return '┌'; }
    @Override
    public char topRight() { return '┐'; }
    @Override
    public char bottomLeft() { return '└'; }
    @Override
    public char bottomRight() { return '┘'; }

    @Override
    public char intersection() { return '┼'; }

    @Override
    public char teeTop() { return '┬'; }
    @Override
    public char teeBottom() { return '┴'; }
    @Override
    public char teeLeft() { return '├'; }
    @Override
    public char teeRight() { return '┤'; }

    @Override
    public int paddingLeft() { return 1; }
    @Override
    public int paddingRight() { return 1; }
}
