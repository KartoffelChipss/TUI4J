package org.strassburger.tui4j.formatting.layout;

import org.strassburger.tui4j.formatting.StyledText;

/**
 * Style configuration for a progress bar (immutable)
 */
public class ProgressBarStyle {
    private final StyledText startDelimiter;
    private final StyledText endDelimiter;
    private final StyledText fillChar;
    private final StyledText emptyChar;
    private final StyledText pointerChar;

    private ProgressBarStyle(Builder builder) {
        this.startDelimiter = builder.startDelimiter;
        this.endDelimiter = builder.endDelimiter;
        this.fillChar = builder.fillChar;
        this.emptyChar = builder.emptyChar;
        this.pointerChar = builder.pointerChar;
    }

    public StyledText getStartDelimiter() {
        return startDelimiter;
    }

    public StyledText getEndDelimiter() {
        return endDelimiter;
    }

    public StyledText getFillChar() {
        return fillChar;
    }

    public StyledText getEmptyChar() {
        return emptyChar;
    }

    public StyledText getPointerChar() {
        return pointerChar;
    }

    /**
     * Builder for ProgressBarStyle
     */
    public static class Builder {
        private StyledText startDelimiter;
        private StyledText endDelimiter;
        private StyledText fillChar;
        private StyledText emptyChar;
        private StyledText pointerChar;

        private void validateSingleChar(StyledText text, String name) {
            if (text == null) throw new IllegalArgumentException(name + " cannot be null");
            if (text.length() != 1) throw new IllegalArgumentException(name + " must be a single character");
        }

        public Builder setStartDelimiter(StyledText startDelimiter) {
            validateSingleChar(startDelimiter, "startDelimiter");
            this.startDelimiter = startDelimiter;
            return this;
        }

        public Builder setEndDelimiter(StyledText endDelimiter) {
            validateSingleChar(endDelimiter, "endDelimiter");
            this.endDelimiter = endDelimiter;
            return this;
        }

        public Builder setFillChar(StyledText fillChar) {
            validateSingleChar(fillChar, "fillChar");
            this.fillChar = fillChar;
            return this;
        }

        public Builder setEmptyChar(StyledText emptyChar) {
            validateSingleChar(emptyChar, "emptyChar");
            this.emptyChar = emptyChar;
            return this;
        }

        public Builder setPointerChar(StyledText pointerChar) {
            validateSingleChar(pointerChar, "pointerChar");
            this.pointerChar = pointerChar;
            return this;
        }

        public ProgressBarStyle build() {
            if (startDelimiter == null) startDelimiter = StyledText.text("[");
            if (endDelimiter == null) endDelimiter = StyledText.text("]");
            if (fillChar == null) fillChar = StyledText.text("=");
            if (emptyChar == null) emptyChar = StyledText.text(" ");
            if (pointerChar == null) pointerChar = fillChar;

            return new ProgressBarStyle(this);
        }
    }

    public static final ProgressBarStyle DEFAULT = new Builder()
            .setStartDelimiter(StyledText.text("["))
            .setEndDelimiter(StyledText.text("]"))
            .setFillChar(StyledText.text("="))
            .setEmptyChar(StyledText.text(" "))
            .setPointerChar(StyledText.text("="))
            .build();

    public static final ProgressBarStyle ARROW = new Builder()
            .setStartDelimiter(StyledText.text("["))
            .setEndDelimiter(StyledText.text("]"))
            .setFillChar(StyledText.text("-"))
            .setEmptyChar(StyledText.text(" "))
            .setPointerChar(StyledText.text(">"))
            .build();

    public static final ProgressBarStyle BLOCK = new Builder()
            .setStartDelimiter(StyledText.text("["))
            .setEndDelimiter(StyledText.text("]"))
            .setFillChar(StyledText.text("█"))
            .setEmptyChar(StyledText.text("░"))
            .setPointerChar(StyledText.text("█"))
            .build();

    public static final ProgressBarStyle SQUARES = new Builder()
            .setStartDelimiter(StyledText.text("["))
            .setEndDelimiter(StyledText.text("]"))
            .setFillChar(StyledText.text("■"))
            .setEmptyChar(StyledText.text("□"))
            .setPointerChar(StyledText.text("■"))
            .build();

    public static final ProgressBarStyle DASHED = new Builder()
            .setStartDelimiter(StyledText.text("["))
            .setEndDelimiter(StyledText.text("]"))
            .setFillChar(StyledText.text("="))
            .setEmptyChar(StyledText.text("-"))
            .setPointerChar(StyledText.text(">"))
            .build();

    public static final ProgressBarStyle BRACKET = new Builder()
            .setStartDelimiter(StyledText.text("<"))
            .setEndDelimiter(StyledText.text(">"))
            .setFillChar(StyledText.text("#"))
            .setEmptyChar(StyledText.text("."))
            .setPointerChar(StyledText.text("#"))
            .build();

    public static final ProgressBarStyle CIRCLE = new Builder()
            .setStartDelimiter(StyledText.text("("))
            .setEndDelimiter(StyledText.text(")"))
            .setFillChar(StyledText.text("●"))
            .setEmptyChar(StyledText.text("○"))
            .setPointerChar(StyledText.text("●"))
            .build();

    public static final ProgressBarStyle SLASHED = new Builder()
            .setStartDelimiter(StyledText.text("["))
            .setEndDelimiter(StyledText.text("]"))
            .setFillChar(StyledText.text("/"))
            .setEmptyChar(StyledText.text("\\"))
            .setPointerChar(StyledText.text("/"))
            .build();

    public static final ProgressBarStyle STAR = new Builder()
            .setStartDelimiter(StyledText.text("["))
            .setEndDelimiter(StyledText.text("]"))
            .setFillChar(StyledText.text("*"))
            .setEmptyChar(StyledText.text("."))
            .setPointerChar(StyledText.text("*"))
            .build();

    public static final ProgressBarStyle FUTURISTIC = new Builder()
            .setStartDelimiter(StyledText.text(" "))
            .setEndDelimiter(StyledText.text(" "))
            .setFillChar(StyledText.text("▰"))
            .setEmptyChar(StyledText.text("▱"))
            .setPointerChar(StyledText.text("▰"))
            .build();
}