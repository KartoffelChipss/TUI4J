package org.strassburger.tui4j.printer;

import org.strassburger.tui4j.formatting.PlainTextRenderer;
import org.strassburger.tui4j.formatting.StyledText;
import org.strassburger.tui4j.formatting.StyledTextRenderer;
import org.strassburger.tui4j.formatting.layout.Renderable;

/**
 * A Printer implementation that captures output in memory instead of writing to the console.
 */
public class TestPrinter implements Printer {
    private final StringBuilder output = new StringBuilder();
    private final StyledTextRenderer textRenderer;

    private int width = 80;
    private int height = 24;
    private int clearCount = 0;

    /**
     * Creates a TestPrinter using a PlainTextRenderer for StyledText, so captured output contains no ANSI codes.
     */
    public TestPrinter() {
        this.textRenderer = new PlainTextRenderer();
    }

    /**
     * Creates a TestPrinter with a custom StyledTextRenderer
     * @param renderer the StyledTextRenderer to use
     */
    public TestPrinter(StyledTextRenderer renderer) {
        this.textRenderer = renderer;
    }

    @Override
    public void print(char c) {
        output.append(c);
    }

    @Override
    public void print(String text) {
        output.append(text);
    }

    @Override
    public void print(StyledText text) {
        output.append(textRenderer.render(text));
    }

    @Override
    public void print(Renderable renderable) {
        renderable.render(this, width, height);
    }

    @Override
    public void println() {
        output.append(System.lineSeparator());
    }

    @Override
    public void println(char c) {
        output.append(c).append(System.lineSeparator());
    }

    @Override
    public void println(String text) {
        output.append(text).append(System.lineSeparator());
    }

    @Override
    public void println(StyledText text) {
        output.append(textRenderer.render(text)).append(System.lineSeparator());
    }

    @Override
    public void println(Renderable renderable) {
        renderable.render(this, width, height);
        output.append(System.lineSeparator());
    }

    @Override
    public void printf(String format, Object... args) {
        output.append(String.format(format, args));
    }

    @Override
    public void printfln(String format, Object... args) {
        output.append(String.format(format, args)).append(System.lineSeparator());
    }

    @Override
    public void printf(StyledText format, Object... args) {
        output.append(String.format(textRenderer.render(format), args));
    }

    @Override
    public void printfln(StyledText format, Object... args) {
        output.append(String.format(textRenderer.render(format), args)).append(System.lineSeparator());
    }

    @Override
    public void clear() {
        clearCount++;
        output.setLength(0);
    }

    /**
     * Returns everything printed so far as a single string.
     * @return the captured output
     */
    public String getOutput() {
        return output.toString();
    }

    /**
     * Clears the captured output without counting as a clear() call.
     */
    public void reset() {
        output.setLength(0);
        clearCount = 0;
    }

    /**
     * Returns how many times clear() was called.
     * @return the clear call count
     */
    public int getClearCount() {
        return clearCount;
    }

    /**
     * Sets the fake terminal width used when rendering Renderable objects.
     * @param width the width to report
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Sets the fake terminal height used when rendering Renderable objects.
     * @param height the height to report
     */
    public void setHeight(int height) {
        this.height = height;
    }

    public StyledTextRenderer getTextRenderer() {
        return textRenderer;
    }
}