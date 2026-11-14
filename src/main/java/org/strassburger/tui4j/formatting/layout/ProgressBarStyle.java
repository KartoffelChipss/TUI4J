package org.strassburger.tui4j.formatting.layout;

import org.jspecify.annotations.Nullable;
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
        if (builder.startDelimiter == null) throw new IllegalArgumentException("startDelimiter must not be null");
        if (builder.endDelimiter == null) throw new IllegalArgumentException("endDelimiter must not be null");
        if (builder.fillChar == null) throw new IllegalArgumentException("fillChar must not be null");
        if (builder.emptyChar == null) throw new IllegalArgumentException("emptyChar must not be null");
        if (builder.pointerChar == null) throw new IllegalArgumentException("pointerChar must not be null");
        this.startDelimiter = builder.startDelimiter;
        this.endDelimiter = builder.endDelimiter;
        this.fillChar = builder.fillChar;
        this.emptyChar = builder.emptyChar;
        this.pointerChar = builder.pointerChar;
    }

    public StyledText startDelimiter() {
        return startDelimiter;
    }

    public StyledText endDelimiter() {
        return endDelimiter;
    }

    public StyledText fillChar() {
        return fillChar;
    }

    public StyledText emptyChar() {
        return emptyChar;
    }

    public StyledText pointerChar() {
        return pointerChar;
    }

    /**
     * Builder for ProgressBarStyle
     */
    public static class Builder {
        private @Nullable StyledText startDelimiter;
        private @Nullable StyledText endDelimiter;
        private @Nullable StyledText fillChar;
        private @Nullable StyledText emptyChar;
        private @Nullable StyledText pointerChar;

        private void validateSingleChar(StyledText text, String name) {
            if (text.length() != 1) throw new IllegalArgumentException(name + " must be a single character");
        }

        public Builder startDelimiter(StyledText startDelimiter) {
            validateSingleChar(startDelimiter, "startDelimiter");
            this.startDelimiter = startDelimiter;
            return this;
        }

        public Builder endDelimiter(StyledText endDelimiter) {
            validateSingleChar(endDelimiter, "endDelimiter");
            this.endDelimiter = endDelimiter;
            return this;
        }

        public Builder fillChar(StyledText fillChar) {
            validateSingleChar(fillChar, "fillChar");
            this.fillChar = fillChar;
            return this;
        }

        public Builder emptyChar(StyledText emptyChar) {
            validateSingleChar(emptyChar, "emptyChar");
            this.emptyChar = emptyChar;
            return this;
        }

        public Builder pointerChar(StyledText pointerChar) {
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
            .startDelimiter(StyledText.text("["))
            .endDelimiter(StyledText.text("]"))
            .fillChar(StyledText.text("="))
            .emptyChar(StyledText.text(" "))
            .pointerChar(StyledText.text("="))
            .build();

    public static final ProgressBarStyle ARROW = new Builder()
            .startDelimiter(StyledText.text("["))
            .endDelimiter(StyledText.text("]"))
            .fillChar(StyledText.text("-"))
            .emptyChar(StyledText.text(" "))
            .pointerChar(StyledText.text(">"))
            .build();

    public static final ProgressBarStyle BLOCK = new Builder()
            .startDelimiter(StyledText.text("["))
            .endDelimiter(StyledText.text("]"))
            .fillChar(StyledText.text("█"))
            .emptyChar(StyledText.text("░"))
            .pointerChar(StyledText.text("█"))
            .build();

    public static final ProgressBarStyle SQUARES = new Builder()
            .startDelimiter(StyledText.text("["))
            .endDelimiter(StyledText.text("]"))
            .fillChar(StyledText.text("■"))
            .emptyChar(StyledText.text("□"))
            .pointerChar(StyledText.text("■"))
            .build();

    public static final ProgressBarStyle DASHED = new Builder()
            .startDelimiter(StyledText.text("["))
            .endDelimiter(StyledText.text("]"))
            .fillChar(StyledText.text("="))
            .emptyChar(StyledText.text("-"))
            .pointerChar(StyledText.text(">"))
            .build();

    public static final ProgressBarStyle BRACKET = new Builder()
            .startDelimiter(StyledText.text("<"))
            .endDelimiter(StyledText.text(">"))
            .fillChar(StyledText.text("#"))
            .emptyChar(StyledText.text("."))
            .pointerChar(StyledText.text("#"))
            .build();

    public static final ProgressBarStyle CIRCLE = new Builder()
            .startDelimiter(StyledText.text("("))
            .endDelimiter(StyledText.text(")"))
            .fillChar(StyledText.text("●"))
            .emptyChar(StyledText.text("○"))
            .pointerChar(StyledText.text("●"))
            .build();

    public static final ProgressBarStyle SLASHED = new Builder()
            .startDelimiter(StyledText.text("["))
            .endDelimiter(StyledText.text("]"))
            .fillChar(StyledText.text("/"))
            .emptyChar(StyledText.text("\\"))
            .pointerChar(StyledText.text("/"))
            .build();

    public static final ProgressBarStyle STAR = new Builder()
            .startDelimiter(StyledText.text("["))
            .endDelimiter(StyledText.text("]"))
            .fillChar(StyledText.text("*"))
            .emptyChar(StyledText.text("."))
            .pointerChar(StyledText.text("*"))
            .build();

    public static final ProgressBarStyle FUTURISTIC = new Builder()
            .startDelimiter(StyledText.text(" "))
            .endDelimiter(StyledText.text(" "))
            .fillChar(StyledText.text("▰"))
            .emptyChar(StyledText.text("▱"))
            .pointerChar(StyledText.text("▰"))
            .build();
}