package org.strassburger.tui4j.formatting;

import org.jspecify.annotations.Nullable;
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

    private StyledText(@Nullable List<Span> spans) {
        this.spans = spans == null
                ? List.of()
                : List.copyOf(mergeSpans(spans));
    }

    public List<Span> spans() {
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
    public static StyledText text(String text, @Nullable Style style) {
        Objects.requireNonNull(text, "Text cannot be null");
        return new StyledText(List.of(new Span(text, style != null ? style : new Style())));
    }


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


    /**
     * Replace occurrences of a target string with a replacement string in all spans
     * @param target the string to be replaced
     * @param replacement the string to replace with
     * @return a new StyledText instance with the replacements made
     */
    public StyledText replace(String target, String replacement) {
        Objects.requireNonNull(target, "Target cannot be null");
        Objects.requireNonNull(replacement, "Replacement cannot be null");

        List<Span> newSpans = new ArrayList<>();

        for (Span span : spans) {
            String modifiedText = span.text().replace(target, replacement);
            newSpans.add(new Span(modifiedText, span.style()));
        }

        return new StyledText(newSpans);
    }

    /**
     * Get the total length of the entire StyledText
     * @return the length of the text
     */
    public int length() {
        int len = 0;
        for (Span span : spans) {
            len += span.text().length();
        }
        return len;
    }

    /**
     * Get a substring of the entire StyledText, keeping the styles of the spans it covers
     * @param beginIndex the beginning index, inclusive
     * @param endIndex the ending index, exclusive
     * @return a new StyledText instance representing the substring
     * @throws IndexOutOfBoundsException if the indices are out of range or beginIndex is greater than endIndex
     */
    public StyledText substring(int beginIndex, int endIndex) {
        int length = length();
        if (beginIndex < 0 || endIndex > length || beginIndex > endIndex) {
            throw new IndexOutOfBoundsException(
                    "begin " + beginIndex + ", end " + endIndex + ", length " + length);
        }
        if (beginIndex == endIndex) return new StyledText(List.of());

        List<Span> result = new ArrayList<>();
        int offset = 0;
        for (Span span : spans) {
            int spanStart = offset;
            int spanEnd = offset + span.text().length();
            offset = spanEnd;

            if (spanEnd <= beginIndex) continue;
            if (spanStart >= endIndex) break;

            int from = Math.max(beginIndex, spanStart) - spanStart;
            int to = Math.min(endIndex, spanEnd) - spanStart;
            result.add(new Span(span.text().substring(from, to), span.style()));
        }
        return new StyledText(result);
    }

    /**
     * Get the plain text content without any styling
     * @return the plain text
     */
    public String plainText() {
        StringBuilder sb = new StringBuilder();
        for (Span span : spans) sb.append(span.text());
        return sb.toString();
    }

    /**
     * <p>
     * Wrap the text into lines of at most the given width, keeping all styles.
     * </p>
     * <p>
     * Lines are broken at spaces where possible; words longer than the width are split.
     * Existing line breaks ({@code \n}) are kept. The spaces at a soft line break are dropped.
     * </p>
     * @param width the maximum line length (must be at least 1)
     * @return the wrapped lines (never empty)
     */
    public List<StyledText> wrap(int width) {
        if (width < 1) throw new IllegalArgumentException("width must be at least 1");

        String plain = plainText();
        List<StyledText> lines = new ArrayList<>();

        int paragraphStart = 0;
        while (true) {
            int newline = plain.indexOf('\n', paragraphStart);
            int paragraphEnd = newline == -1 ? plain.length() : newline;
            wrapParagraph(plain, paragraphStart, paragraphEnd, width, lines);
            if (newline == -1) break;
            paragraphStart = newline + 1;
        }

        return lines;
    }

    private void wrapParagraph(String plain, int start, int end, int width, List<StyledText> lines) {
        int pos = start;
        while (end - pos > width) {
            // find the last space at which we can break so the line fits
            int breakAt = -1;
            for (int i = pos + width; i > pos; i--) {
                if (plain.charAt(i) == ' ') {
                    breakAt = i;
                    break;
                }
            }

            if (breakAt == -1) {
                // no space to break at: hard-break the word
                lines.add(substring(pos, pos + width));
                pos += width;
            } else {
                int lineEnd = breakAt;
                while (lineEnd > pos && plain.charAt(lineEnd - 1) == ' ') lineEnd--;
                lines.add(substring(pos, lineEnd));
                pos = breakAt;
                while (pos < end && plain.charAt(pos) == ' ') pos++;
            }
        }
        lines.add(substring(pos, end));
    }

    /**
     * Repeat the entire StyledText a specified number of times
     * @param count the number of times to repeat
     * @return a new StyledText instance with the repeated content
     */
    public StyledText repeat(int count) {
        if (count <= 0) {
            return new StyledText(List.of());
        }

        List<Span> newSpans = new ArrayList<>(spans.size() * count);
        for (int i = 0; i < count; i++) {
            newSpans.addAll(spans);
        }

        return new StyledText(newSpans);
    }


    private StyledText modifyLast(Function<Style, Style> modifier) {
        if (spans.isEmpty()) return this;

        List<Span> newSpans = new ArrayList<>(spans.size());
        newSpans.addAll(spans.subList(0, spans.size() - 1));

        Span last = spans.get(spans.size() - 1);
        newSpans.add(last.withStyle(modifier.apply(last.style())));

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
     * Underline the last span
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

    /**
     * Make the last span italic
     * @return the modified StyledText
     */
    public StyledText italic() {
        return modifyLast(Style::italic);
    }

    /**
     * Make the last span dim
     * @return the modified StyledText
     */
    public StyledText dim() {
        return modifyLast(Style::dim);
    }

    /**
     * Make the last span blink
     * <p>
     * Note: Blinking text is not supported by every terminal and is often disabled by default.
     * If the text does not blink, check the terminal settings. On macOS, both Terminal and iTerm2
     * have a "Blinking text" option under Settings &rarr; Profiles &rarr; Text.
     * </p>
     * @return the modified StyledText
     */
    public StyledText blinking() {
        return modifyLast(Style::blinking);
    }

    /**
     * Hide the last span (ANSI conceal). The text still takes up space and can be selected or copied,
     * so this must not be used to hide sensitive information such as passwords.
     * @return the modified StyledText
     */
    public StyledText hidden() {
        return modifyLast(Style::hidden);
    }

    private static List<Span> mergeSpans(List<Span> spans) {
        if (spans.isEmpty()) return List.of();

        List<Span> merged = new ArrayList<>();
        Span current = spans.get(0);

        for (int i = 1; i < spans.size(); i++) {
            Span next = spans.get(i);

            if (current.style().equals(next.style())) {
                current = new Span(
                        current.text() + next.text(),
                        current.style()
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