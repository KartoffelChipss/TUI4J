package org.strassburger.tui4j.formatting.layout;

import org.jspecify.annotations.Nullable;
import org.strassburger.colorlab4j.color.Color;
import org.strassburger.tui4j.formatting.StyledText;
import org.strassburger.tui4j.console.Printer;

/**
 * A terminal progress bar animation
 */
public class ProgressBar implements Renderable {
    private StyledText message = StyledText.text("Loading...");
    private @Nullable Color barColor = null;
    private ProgressBarStyle style = ProgressBarStyle.SQUARES;

    private volatile int progress = 0;
    private @Nullable Printer printer;
    private int width = 0;

    /**
     * Create a progress bar
     *
     * <p>Example usage:</p>
     * <pre>
     * {@code
     * ProgressBar progressBar = new ProgressBar()
     *          .message(StyledText.text("Loading some stuff..."))
     *          .style(ProgressBarStyle.SQUARES);
     * printer.print(progressBar);
     *
     * for (int i = 0; i <= 100; i++) {
     *     Thread.sleep(50);
     *     progressBar.progress(i);
     *     progressBar.message(StyledText.text("Loading some stuff... " + i + "%"));
     * }
     *
     * progressBar.complete(StyledText.text("✔ Loading complete!").fg(AnsiColor.GREEN).bold());
     * }
     * </pre>
     */
    public ProgressBar() {}

    /**
     * Create a progress bar with a custom message
     *
     * <p>Example usage:</p>
     * <pre>
     * {@code
     * ProgressBar progressBar = new ProgressBar(StyledText.text("Loading some stuff..."))
     *          .style(ProgressBarStyle.SQUARES);
     * printer.print(progressBar);
     *
     * for (int i = 0; i <= 100; i++) {
     *     Thread.sleep(50);
     *     progressBar.progress(i);
     *     progressBar.message(StyledText.text("Loading some stuff... " + i + "%"));
     * }
     *
     * progressBar.complete(StyledText.text("✔ Loading complete!").fg(AnsiColor.GREEN).bold());
     * }
     * </pre>
     * @param message The message to display next to the spinner
     */
    public ProgressBar(StyledText message) {
        this.message = message;
    }

    public StyledText message() {
        return message;
    }

    /**
     * Set the message displayed next to the progress bar
     */
    public ProgressBar message(StyledText message) {
        this.message = message;
        return this;
    }

    public @Nullable Color barColor() {
        return barColor;
    }

    /**
     * Overwrite the color of the entire progress bar
     */
    public ProgressBar barColor(Color barColor) {
        this.barColor = barColor;
        return this;
    }

    public ProgressBarStyle style() {
        return style;
    }

    public ProgressBar style(ProgressBarStyle style) {
        this.style = style;
        return this;
    }

    /**
     * Update the progress percentage. Clamped between 0 and 100.
     */
    public ProgressBar progress(int progress) {
        this.progress = Math.min(100, Math.max(0, progress)); // Clamp 0-100
        renderBar();
        return this;
    }

    public int progress() {
        return progress;
    }

    /**
     * Complete the progress bar and display a final message
     * @param finalMessage The final message to display
     */
    public void complete(StyledText finalMessage) {
        complete(finalMessage, false);
    }

    /**
     * Complete the progress bar and optionally display a final message
     * @param finalMessage The final message to display
     * @param clearAfter If true, clears the progress bar after completion
     */
    public void complete(StyledText finalMessage, boolean clearAfter) {
        if (printer == null) throw new IllegalStateException("ProgressBar not initialized. Call render() first.");
        if (finalMessage == null) throw new IllegalArgumentException("finalMessage cannot be null");
        progress(100);

        if (clearAfter) {
            int spacesToAppend = width - finalMessage.length();
            printer.println(finalMessage
                    .prepend("\r")
                    .append(" ".repeat(Math.max(0, spacesToAppend)))
            );
        } else {
            message = finalMessage;
            renderBar();
            printer.println();
        }
    }

    private void renderBar() {
        if (printer == null) throw new IllegalStateException("ProgressBar not initialized. Call render() first.");
        // Minimum width for the bar itself
        int minBarWidth = width / 4;
        if (message.length() > width - minBarWidth) {
            message = message.substring(0, width - minBarWidth - 3).append("...");
        }

        int lengthForBar = width - message.length() - 3; // 3 for brackets and space
        int filledLength = (int) ((progress / 100.0) * lengthForBar);
        int emptyLength = lengthForBar - filledLength;

        StyledText bar = style.startDelimiter()
                .append(style.fillChar().repeat(filledLength > 0 ? filledLength - 1 : 0))
                .append(filledLength > 0 ? style.pointerChar() : StyledText.text(""))
                .append(style.emptyChar().repeat(emptyLength))
                .append(style.endDelimiter());

        if (barColor != null) bar = bar.fg(barColor);

        StyledText text = bar.append(" ").append(message);
        printer.print(text.prepend("\r"));
    }

    /**
     * Render the progress bar (initial print)
     */
    @Override
    public void render(Printer printer, int width, int height) {
        this.printer = printer;
        this.width = width;
        renderBar();
    }
}