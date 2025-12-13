package org.strassburger.tui4j.formatting;

import org.strassburger.colorlab4j.color.Color;
import org.strassburger.tui4j.formatting.ansi.AnsiCode;

import java.util.Objects;

/**
 * Represents text style attributes.
 */
public final class Style {
    private final Color foreground;
    private final Color background;
    private final boolean bold;
    private final boolean underline;
    private final boolean inverse;

    /**
     * Default style
     */
    public Style() {
        this(null, null, false, false, false);
    }

    private Style(
            Color foreground,
            Color background,
            boolean bold,
            boolean underline,
            boolean inverse
    ) {
        this.foreground = foreground;
        this.background = background;
        this.bold = bold;
        this.underline = underline;
        this.inverse = inverse;
    }

    /**
     * Copy constructor
     */
    public Style(Style other) {
        this(
                other.foreground,
                other.background,
                other.bold,
                other.underline,
                other.inverse
        );
    }

    /**
     * Convert this style to ANSI escape codes
     * @return the ANSI escape codes representing this style
     */
    public String toAnsiCodes() {
        StringBuilder codes = new StringBuilder();

        if (bold) codes.append(AnsiCode.BOLD);
        if (underline) codes.append(AnsiCode.UNDERLINE);
        if (inverse) codes.append(AnsiCode.INVERSE);
        if (foreground != null) codes.append(foreground.toAnsi());
        if (background != null) codes.append(background.toAnsiBackground());

        return codes.toString();
    }


    public Style fg(Color color) {
        Objects.requireNonNull(color, "Foreground color cannot be null");
        return new Style(color, background, bold, underline, inverse);
    }

    public Style bg(Color color) {
        Objects.requireNonNull(color, "Background color cannot be null");
        return new Style(foreground, color, bold, underline, inverse);
    }

    public Style bold() {
        return new Style(foreground, background, true, underline, inverse);
    }

    public Style underline() {
        return new Style(foreground, background, bold, true, inverse);
    }

    public Style inverse() {
        return new Style(foreground, background, bold, underline, true);
    }


    public Color getForeground() {
        return foreground;
    }

    public Color getBackground() {
        return background;
    }

    public boolean isBold() {
        return bold;
    }

    public boolean isUnderline() {
        return underline;
    }

    public boolean isInverse() {
        return inverse;
    }

    @Override
    public String toString() {
        return "Style{" +
                "foreground=" + foreground +
                ", background=" + background +
                ", bold=" + bold +
                ", underline=" + underline +
                ", inverse=" + inverse +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Style style)) return false;
        return bold == style.bold &&
                underline == style.underline &&
                inverse == style.inverse &&
                Objects.equals(foreground, style.foreground) &&
                Objects.equals(background, style.background);
    }

    @Override
    public int hashCode() {
        return Objects.hash(foreground, background, bold, underline, inverse);
    }
}