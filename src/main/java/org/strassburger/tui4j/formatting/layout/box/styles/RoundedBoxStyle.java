package org.strassburger.tui4j.formatting.layout.box.styles;

/**
 * A BoxStyle using Unicode box-drawing characters with rounded corners.
 */
public class RoundedBoxStyle extends ColorableBoxStyle {
    @Override
    public char verticalLeft() { return '│'; }
    @Override
    public char verticalRight() { return '│'; }

    @Override
    public char horizontalTop() { return '─'; }
    @Override
    public char horizontalBottom() { return '─'; }

    @Override
    public char topLeft() { return '╭'; }
    @Override
    public char topRight() { return '╮'; }
    @Override
    public char bottomLeft() { return '╰'; }
    @Override
    public char bottomRight() { return '╯'; }
}
