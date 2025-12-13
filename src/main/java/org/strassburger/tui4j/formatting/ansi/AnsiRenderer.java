package org.strassburger.tui4j.formatting.ansi;

import org.strassburger.tui4j.formatting.Span;
import org.strassburger.tui4j.formatting.StyledText;
import org.strassburger.tui4j.formatting.StyledTextRenderer;

public class AnsiRenderer implements StyledTextRenderer {
    /**
     * Render the given StyledText to a string with ANSI escape codes
     * @param styledText the styled text to render
     * @return the ANSI encoded string
     */
    @Override
    public String render(StyledText styledText) {
        StringBuilder sb = new StringBuilder();
        for (Span span : styledText.getSpans()) {
            sb.append(span.getStyle().toAnsiCodes());
            sb.append(span.getText());
            sb.append(AnsiCode.RESET);
        }
        return sb.toString();
    }
}
