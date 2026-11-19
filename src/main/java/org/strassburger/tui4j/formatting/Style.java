package org.strassburger.tui4j.formatting;

import org.jspecify.annotations.Nullable;
import org.strassburger.colorlab4j.color.Color;
import org.strassburger.tui4j.formatting.ansi.AnsiCode;

import java.util.Objects;

/**
 * Represents text style attributes.
 */
public final class Style {
    private final @Nullable Color foreground;
    private final @Nullable Color background;
    private final boolean bold;
    private final boolean underline;
    private final boolean inverse;
    private final boolean italic;
    private final boolean dim;
    private final boolean blinking;
    private final boolean hidden;

    /**
     * Default style
     */
    public Style() {
        this(null, null, false, false, false, false, false, false, false);
    }

    private Style(
            @Nullable Color foreground,
            @Nullable Color background,
            boolean bold,
            boolean underline,
            boolean inverse,
            boolean italic,
            boolean dim,
            boolean blinking,
            boolean hidden
    ) {
        this.foreground = foreground;
        this.background = background;
        this.bold = bold;
        this.underline = underline;
        this.inverse = inverse;
        this.italic = italic;
        this.dim = dim;
        this.blinking = blinking;
        this.hidden = hidden;
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
                other.inverse,
                other.italic,
                other.dim,
                other.blinking,
                other.hidden
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
        if (italic) codes.append(AnsiCode.ITALIC);
        if (dim) codes.append(AnsiCode.DIM);
        if (blinking) codes.append(AnsiCode.BLINK);
        if (hidden) codes.append(AnsiCode.HIDDEN);
        if (foreground != null) codes.append(foreground.toAnsi());
        if (background != null) codes.append(background.toAnsiBackground());

        return codes.toString();
    }


    public Style fg(Color color) {
        Objects.requireNonNull(color, "Foreground color cannot be null");
        return new Style(color, background, bold, underline, inverse, italic, dim, blinking, hidden);
    }

    public Style bg(Color color) {
        Objects.requireNonNull(color, "Background color cannot be null");
        return new Style(foreground, color, bold, underline, inverse, italic, dim, blinking, hidden);
    }

    public Style bold() {
        return new Style(foreground, background, true, underline, inverse, italic, dim, blinking, hidden);
    }

    public Style underline() {
        return new Style(foreground, background, bold, true, inverse, italic, dim, blinking, hidden);
    }

    public Style inverse() {
        return new Style(foreground, background, bold, underline, true, italic, dim, blinking, hidden);
    }

    public Style italic() {
        return new Style(foreground, background, bold, underline, inverse, true, dim, blinking, hidden);
    }

    public Style dim() {
        return new Style(foreground, background, bold, underline, inverse, italic, true, blinking, hidden);
    }

    /**
     * Make the text blink
     * <p>
     * Note: Blinking text is not supported by every terminal and is often disabled by default.
     * If the text does not blink, check the terminal settings. On macOS, both Terminal and iTerm2
     * have a "Blinking text" option under Settings &rarr; Profiles &rarr; Text.
     * </p>
     * @return a new Style with blinking enabled
     */
    public Style blinking() {
        return new Style(foreground, background, bold, underline, inverse, italic, dim, true, hidden);
    }

    /**
     * Hide the text (ANSI conceal). The text still takes up space and can be selected or copied,
     * so this must not be used to hide sensitive information such as passwords.
     * @return a new Style with hidden enabled
     */
    public Style hidden() {
        return new Style(foreground, background, bold, underline, inverse, italic, dim, blinking, true);
    }


    public @Nullable Color foreground() {
        return foreground;
    }

    public @Nullable Color background() {
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

    public boolean isItalic() {
        return italic;
    }

    public boolean isDim() {
        return dim;
    }

    public boolean isBlinking() {
        return blinking;
    }

    public boolean isHidden() {
        return hidden;
    }

    @Override
    public String toString() {
        return "Style{" +
                "foreground=" + foreground +
                ", background=" + background +
                ", bold=" + bold +
                ", underline=" + underline +
                ", inverse=" + inverse +
                ", italic=" + italic +
                ", dim=" + dim +
                ", blinking=" + blinking +
                ", hidden=" + hidden +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Style style)) return false;
        return bold == style.bold &&
                underline == style.underline &&
                inverse == style.inverse &&
                italic == style.italic &&
                dim == style.dim &&
                blinking == style.blinking &&
                hidden == style.hidden &&
                Objects.equals(foreground, style.foreground) &&
                Objects.equals(background, style.background);
    }

    @Override
    public int hashCode() {
        return Objects.hash(foreground, background, bold, underline, inverse, italic, dim, blinking, hidden);
    }
}