package org.strassburger.tui4j.printer;

import org.strassburger.tui4j.formatting.StyledText;
import org.strassburger.tui4j.formatting.StyledTextRenderer;
import org.strassburger.tui4j.formatting.ansi.AnsiRenderer;

/**
 * A Printer implementation that outputs to the console (System.out)
 */
public class ConsolePrinter implements Printer {
    private final StyledTextRenderer textRenderer;

    /**
     * Creates a ConsolePrinter with the default AnsiRenderer
     */
    public ConsolePrinter() {
        this.textRenderer = new AnsiRenderer();
    }

    /**
     * Creates a ConsolePrinter with a custom StyledTextRenderer
     * @param renderer the StyledTextRenderer to use for rendering styled text
     */
    public ConsolePrinter(StyledTextRenderer renderer) {
        this.textRenderer = renderer;
    }

    @Override
    public void print(String text) {
        System.out.print(text);
    }

    @Override
    public void print(StyledText text) {
        System.out.print(textRenderer.render(text));
    }

    @Override
    public void println() {
        System.out.println();
    }

    @Override
    public void println(String text) {
        System.out.println(text);
    }

    @Override
    public void println(StyledText text) {
        System.out.println(textRenderer.render(text));
    }

    @Override
    public void printf(String format, Object... args) {
        System.out.printf(format, args);
    }

    @Override
    public void printfln(String format, Object... args) {
        System.out.printf(format + "%n", args);
    }

    @Override
    public void printf(StyledText format, Object... args) {
        System.out.printf(textRenderer.render(format), args);
    }

    @Override
    public void printfln(StyledText format, Object... args) {
        System.out.printf(textRenderer.render(format) + "%n", args);
    }
}
