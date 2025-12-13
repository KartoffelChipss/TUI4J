package org.strassburger.tui4j.input.exceptions;

import org.strassburger.tui4j.formatting.PlainTextRenderer;
import org.strassburger.tui4j.formatting.StyledText;

public class InputValidationException extends RuntimeException {
    private final StyledText styledMessage;

    public InputValidationException(String message) {
        super(message);
        this.styledMessage = StyledText.text(message);
    }

    public InputValidationException(StyledText styledMessage) {
        super(new PlainTextRenderer().render(styledMessage));
        this.styledMessage = styledMessage;
    }

    public StyledText getStyledMessage() {
        return styledMessage;
    }
}
