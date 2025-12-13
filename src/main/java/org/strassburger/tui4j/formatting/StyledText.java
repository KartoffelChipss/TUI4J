package org.strassburger.tui4j.formatting;

import org.strassburger.colorlab4j.color.Color;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * Represents styled text composed of multiple spans, each with its own style.
 * </p>
 * <p>
 * Example usage:
 * </p>
 * <pre>{@code
 * StyledText styled = StyledText.text("Hello, ")
 *     .fg(Color.fromHex("00ff00"))
 *     .append(StyledText.text("World!").bold())
 *     .underline();
 * }</pre>
 */
public final class StyledText {

    private final List<Span> spans;

    private StyledText(List<Span> spans) {
        this.spans = spans == null
                ? List.of()
                : List.copyOf(mergeSpans(spans));
    }

    public List<Span> getSpans() {
        return spans;
    }

    /* === Factory === */

    /**
     * Create a StyledText with a single span of plain text
     * @param text the text content
     * @return the StyledText instance
     */
    public static StyledText text(String text) {
        Objects.requireNonNull(text, "Text cannot be null");
        return new StyledText(List.of(new Span(text, new Style())));
    }

    /**
     * Create a StyledText with a single span of text and the given style
     * @param text the text content
     * @param style the style to apply
     * @return the StyledText instance
     */
    public static StyledText text(String text, Style style) {
        Objects.requireNonNull(text, "Text cannot be null");
        return new StyledText(List.of(new Span(text, style != null ? style : new Style())));
    }

    /* === Composition === */

    /**
     * Append another StyledText to this one
     * @param other the other StyledText to append
     * @return a new StyledText instance representing the combined text
     */
    public StyledText append(StyledText other) {
        Objects.requireNonNull(other, "Other StyledText cannot be null");
        if (other.spans.isEmpty()) return this;
        if (this.spans.isEmpty()) return other;

        List<Span> combined = new ArrayList<>(this.spans.size() + other.spans.size());
        combined.addAll(this.spans);
        combined.addAll(other.spans);
        return new StyledText(combined);
    }

    /**
     * Prepend another StyledText to this one
     * @param other the other StyledText to prepend
     * @return a new StyledText instance representing the combined text
     */
    public StyledText prepend(StyledText other) {
        Objects.requireNonNull(other, "Other StyledText cannot be null");
        if (other.spans.isEmpty()) return this;
        if (this.spans.isEmpty()) return other;

        List<Span> combined = new ArrayList<>(this.spans.size() + other.spans.size());
        combined.addAll(other.spans);
        combined.addAll(this.spans);
        return new StyledText(combined);
    }

    /**
     * Append a string with the given style to this StyledText
     * @param text the text content
     * @param style the style to apply
     * @return a new StyledText instance representing the combined text
     */
    public StyledText append(String text, Style style) {
        return append(text(text, style));
    }

    /**
     * Append a plain string to this StyledText
     * @param text the text content
     * @return a new StyledText instance representing the combined text
     */
    public StyledText append(String text) {
        return append(text(text));
    }

    /**
     * Prepend a string with the given style to this StyledText
     * @param text the text content
     * @param style the style to apply
     * @return a new StyledText instance representing the combined text
     */
    public StyledText prepend(String text, Style style) {
        return prepend(text(text, style));
    }

    /**
     * Prepend a plain string to this StyledText
     * @param text the text content
     * @return a new StyledText instance representing the combined text
     */
    public StyledText prepend(String text) {
        return prepend(text(text));
    }

    /* === Style modifiers === */

    private StyledText modifyLast(Function<Style, Style> modifier) {
        if (spans.isEmpty()) return this;

        List<Span> newSpans = new ArrayList<>(spans.size());
        newSpans.addAll(spans.subList(0, spans.size() - 1));

        Span last = spans.get(spans.size() - 1);
        newSpans.add(last.withStyle(modifier.apply(last.getStyle())));

        return new StyledText(newSpans);
    }

    /**
     * Set the foreground color of the last span
     * @param color the foreground color
     * @return the modified StyledText
     */
    public StyledText fg(Color color) {
        Objects.requireNonNull(color);
        return modifyLast(s -> s.fg(color));
    }

    /**
     * Set the background color of the last span
     * @param color the background color
     * @return the modified StyledText
     */
    public StyledText bg(Color color) {
        Objects.requireNonNull(color);
        return modifyLast(s -> s.bg(color));
    }

    /**
     * Make the last span bold
     * @return the modified StyledText
     */
    public StyledText bold() {
        return modifyLast(Style::bold);
    }

    /**
     * Make the last span italic
     * @return the modified StyledText
     */
    public StyledText underline() {
        return modifyLast(Style::underline);
    }

    /**
     * Inverse the colors of the last span
     * @return the modified StyledText
     */
    public StyledText reversed() {
        return modifyLast(Style::inverse);
    }

    /* === Span merging === */

    private static List<Span> mergeSpans(List<Span> spans) {
        if (spans.isEmpty()) return List.of();

        List<Span> merged = new ArrayList<>();
        Span current = spans.get(0);

        for (int i = 1; i < spans.size(); i++) {
            Span next = spans.get(i);

            if (current.getStyle().equals(next.getStyle())) {
                current = new Span(
                        current.getText() + next.getText(),
                        current.getStyle()
                );
            } else {
                merged.add(current);
                current = next;
            }
        }

        merged.add(current);
        return merged;
    }

    @Override
    public String toString() {
        return "[" + spans.stream()
                .map(Span::toString)
                .collect(Collectors.joining(", ")) + "]";
    }
}