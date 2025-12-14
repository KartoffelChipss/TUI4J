package org.strassburger.tui4j.formatting.layout;

import org.strassburger.colorlab4j.color.Color;
import org.strassburger.tui4j.formatting.PlainTextRenderer;
import org.strassburger.tui4j.formatting.StyledText;
import org.strassburger.tui4j.formatting.ansi.AnsiColor;
import org.strassburger.tui4j.printer.Printer;

import java.util.List;

/**
 * A terminal spinner animation
 */
public class Spinner implements Renderable, Runnable {
    private List<StyledText> spinnerFrames = SpinnerFrames.DEFAULT;
    private StyledText message = StyledText.text("Loading...");
    private Color spinnerColor = AnsiColor.WHITE;
    private int speedMs = 100;

    private volatile boolean running = false;
    private int index = 0;
    private Printer printer;
    private int width = 0;

    /**
     * Create a new spinner with default settings
     *
     * <p>Example usage:</p>
     * <pre>
     * {@code
     * Spinner spinner = new Spinner()
     *         .setMessage(StyledText.text("Loading some stuff..."))
     *         .setSpinnerColor(AnsiColor.CYAN)
     *         .setSpeedMs(100)
     *         .setSpinnerFrames(SpinnerFrames.SYNTHWAVE);
     * printer.print(spinner);
     *
     * for (int i = 0; i < 50; i++) {
     *     try {
     *         Thread.sleep(100);
     *         spinner.setMessage(StyledText.text("Loading some stuff... " + (i + 1) * 2 + "%") );
     *     } catch (InterruptedException e) {
     *         throw new RuntimeException(e);
     *     }
     * }
     *
     * spinner.stop(StyledText.text("✔ Loading complete!").fg(AnsiColor.GREEN).bold());
     * }
     * </pre>
     */
    public Spinner() {}

    /**
     * Create a spinner with a custom message
     *
     * <p>Example usage:</p>
     * <pre>
     * {@code
     * Spinner spinner = new Spinner()
     *         .setMessage(StyledText.text("Loading some stuff..."))
     *         .setSpinnerColor(AnsiColor.CYAN)
     *         .setSpeedMs(100)
     *         .setSpinnerFrames(SpinnerFrames.SYNTHWAVE);
     * printer.print(spinner);
     *
     * for (int i = 0; i < 50; i++) {
     *     try {
     *         Thread.sleep(100);
     *         spinner.setMessage(StyledText.text("Loading some stuff... " + (i + 1) * 2 + "%") );
     *     } catch (InterruptedException e) {
     *         throw new RuntimeException(e);
     *     }
     * }
     *
     * spinner.stop(StyledText.text("✔ Loading complete!").fg(AnsiColor.GREEN).bold());
     * }
     * </pre>
     * @param message The message to display next to the spinner
     */
    public Spinner(StyledText message) {
        this.message = message;
    }

    /**
     * Start the spinner in a new thread
     */
    private void start(Printer printer, int width) {
        this.printer = printer;
        this.width = width;
        running = true;
        new Thread(this).start();
    }

    /**
     * Stop the spinner without printing a final message
     */
    public void stop() {
        running = false;
    }

    /**
     * Stop the spinner and print a final message
     */
    public void stop(StyledText finalMessage) {
        running = false;
        if (printer != null) {
            int spacesToAppend = width - finalMessage.length();
            printer.println(finalMessage
                    .prepend("\r")
                    .append(" ".repeat(Math.max(0, spacesToAppend)))
            );
        }
    }

    /**
     * Update the message displayed next to the spinner
     */
    public Spinner setMessage(StyledText message) {
        this.message = message;
        return this;
    }

    public List<StyledText> getSpinnerFrames() {
        return spinnerFrames;
    }

    /**
     * Set the frames used for the spinner animation.
     * All frames must be non-empty and of equal length.<br/>
     * You can find a collection of predefined spinner frames in the {@link SpinnerFrames} class.
     * @throws IllegalArgumentException if the frames are invalid
     */
    public Spinner setSpinnerFrames(List<StyledText> spinnerFrames) {
        if (!isValidFrames(spinnerFrames)) {
            throw new IllegalArgumentException("Spinner frames must be non-empty and of equal length");
        }
        this.spinnerFrames = spinnerFrames;
        return this;
    }

    private boolean isValidFrames(List<StyledText> frames) {
        if (frames == null || frames.isEmpty()) return false;
        // Verify all frames have the same length
        PlainTextRenderer plainTextRenderer = new PlainTextRenderer();
        int length = plainTextRenderer.render(frames.get(0)).length();
        for (StyledText frame : frames) {
            if (plainTextRenderer.render(frame).length() != length) {
                return false;
            }
        }
        return true;
    }

    public int getSpeedMs() {
        return speedMs;
    }

    /**
     * Set the speed of the spinner animation in milliseconds
     */
    public Spinner setSpeedMs(int speedMs) {
        this.speedMs = speedMs;
        return this;
    }

    public Color getSpinnerColor() {
        return spinnerColor;
    }

    /**
     * Set the color of the spinner
     */
    public Spinner setSpinnerColor(Color spinnerColor) {
        this.spinnerColor = spinnerColor;
        return this;
    }

    /**
     * The spinner animation loop
     */
    @Override
    public void run() {
        while (running) {
            index++;
            if (printer != null) {
                StyledText frame = spinnerFrames.get(index % spinnerFrames.size()).fg(spinnerColor);
                StyledText text = frame.append(" ").append(message);

                if (width > 0 && text.length() > width) {
                    text = text.substring(0, width - 3).append("...");
                }

                printer.print(text.prepend("\r"));
            }

            try {
                Thread.sleep(speedMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    /**
     * Render the spinner (starts the spinner)
     */
    @Override
    public void render(Printer printer, int width, int height) {
        start(printer, width);
    }
}