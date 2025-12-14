package org.strassburger.tui4j.printer;

import org.strassburger.tui4j.formatting.PlainTextRenderer;
import org.strassburger.tui4j.formatting.StyledText;
import org.strassburger.tui4j.formatting.StyledTextRenderer;
import org.strassburger.tui4j.formatting.ansi.AnsiRenderer;
import org.strassburger.tui4j.formatting.layout.Renderable;
import org.strassburger.tui4j.terminal.Terminal;
import org.strassburger.tui4j.terminal.TerminalSize;

/**
 * A Printer implementation that outputs to the console (System.out)
 */
public class ConsolePrinter implements Printer {
    private final Terminal terminal;
    private final StyledTextRenderer textRenderer;

    /**
     * Creates a ConsolePrinter with the default AnsiRenderer
     */
    public ConsolePrinter() {
        this.terminal = new Terminal();
        if (terminal.isAnsiSupported()) this.textRenderer = new AnsiRenderer();
        else this.textRenderer = new PlainTextRenderer();
    }

    /**
     * Creates a ConsolePrinter with a custom StyledTextRenderer
     * @param renderer the StyledTextRenderer to use for rendering styled text
     */
    public ConsolePrinter(StyledTextRenderer renderer) {
        this.terminal = new Terminal();
        this.textRenderer = renderer;
    }

    /**
     * Creates a ConsolePrinter with a custom StyledTextRenderer
     * @param renderer the StyledTextRenderer to use for rendering styled text
     */
    public ConsolePrinter(Terminal terminal, StyledTextRenderer renderer) {
        this.terminal = terminal;
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
    public void print(Renderable renderable) {
        TerminalSize.Size size = terminal.getSize();
        renderable.render(this, size.height(), size.width());
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
    public void println(Renderable renderable) {
        TerminalSize.Size size = terminal.getSize();
        renderable.render(this, size.width(), size.height());
        System.out.println();
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

    public Terminal getTerminal() {
        return terminal;
    }

    public StyledTextRenderer getTextRenderer() {
        return textRenderer;
    }
}
