package org.strassburger.tui4j.console;

import org.strassburger.tui4j.console.terminal.Terminal;
import org.strassburger.tui4j.console.terminal.TerminalSize;
import org.strassburger.tui4j.formatting.PlainTextRenderer;
import org.strassburger.tui4j.formatting.StyledText;
import org.strassburger.tui4j.formatting.StyledTextRenderer;
import org.strassburger.tui4j.formatting.ansi.AnsiCode;
import org.strassburger.tui4j.formatting.ansi.AnsiRenderer;
import org.strassburger.tui4j.formatting.layout.Renderable;

import java.util.Scanner;

public class TerminalConsole implements Console {
    private final Terminal terminal;
    private final StyledTextRenderer textRenderer;
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Creates a TerminalConsole with the default AnsiRenderer
     */
    public TerminalConsole() {
        this.terminal = new Terminal();
        if (terminal.isAnsiSupported()) this.textRenderer = new AnsiRenderer();
        else this.textRenderer = new PlainTextRenderer();
    }

    /**
     * Creates a TerminalConsole with a custom StyledTextRenderer
     * @param renderer the StyledTextRenderer to use for rendering styled text
     */
    public TerminalConsole(StyledTextRenderer renderer) {
        this.terminal = new Terminal();
        this.textRenderer = renderer;
    }

    /**
     * Creates a TerminalConsole with a custom StyledTextRenderer
     * @param renderer the StyledTextRenderer to use for rendering styled text
     */
    public TerminalConsole(Terminal terminal, StyledTextRenderer renderer) {
        this.terminal = terminal;
        this.textRenderer = renderer;
    }

    @Override
    public String readLine() {
        return scanner.nextLine();
    }

    @Override
    public void print(char c) {
        System.out.print(c);
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
        renderable.render(this, size.width(), size.height());
    }

    @Override
    public void println() {
        System.out.println();
    }

    @Override
    public void println(char c) {
        System.out.println(c);
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

    @Override
    public void clear() {
        if (terminal.isAnsiSupported()) {
            System.out.println(AnsiCode.ERASE_DISPLAY_ENTIRE + AnsiCode.CURSOR_HOME.toString());
        } else {
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public StyledTextRenderer getTextRenderer() {
        return textRenderer;
    }
}
