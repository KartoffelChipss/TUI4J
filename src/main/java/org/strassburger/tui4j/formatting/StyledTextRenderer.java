package org.strassburger.tui4j.formatting;

public interface StyledTextRenderer {
    /**
     * Render the given StyledText to a string
     * @param styledText the styled text to render
     * @return the rendered string
     */
    String render(StyledText styledText);
}
