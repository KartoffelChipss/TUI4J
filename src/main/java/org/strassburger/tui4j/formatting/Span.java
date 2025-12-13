package org.strassburger.tui4j.formatting;

import java.util.Objects;

/**
 * A Span represents an immutable segment of text with an associated Style.
 */
public final class Span {
    private final String text;
    private final Style style;

    public Span(String text, Style style) {
        this.text = Objects.requireNonNull(text, "Text cannot be null");
        this.style = style != null ? style : new Style();
    }

    public String getText() {
        return text;
    }

    public Style getStyle() {
        return style;
    }

    /**
     * Create a new Span with the same text but a different style
     */
    public Span withStyle(Style newStyle) {
        return new Span(this.text, newStyle);
    }

    @Override
    public String toString() {
        return "Span{text='" + text + "', style=" + style + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Span span)) return false;
        return text.equals(span.text) && style.equals(span.style);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, style);
    }
}