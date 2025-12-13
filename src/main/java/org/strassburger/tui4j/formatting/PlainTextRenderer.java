package org.strassburger.tui4j.formatting;

/**
 * A StyledTextRenderer implementation that renders plain text without styles
 */
public class PlainTextRenderer implements StyledTextRenderer {
    /**
     * Render the given StyledText to plain text (without styles)
     * @param styledText the styled text to render
     * @return the plain text string
     */
    @Override
    public String render(StyledText styledText) {
        StringBuilder sb = new StringBuilder();
        for (Span span : styledText.getSpans()) {
            sb.append(span.getText());
        }
        return sb.toString();
    }
}
