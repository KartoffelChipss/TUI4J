package org.strassburger.tui4j.console;

import org.strassburger.tui4j.console.terminal.Terminal;
import org.strassburger.tui4j.console.terminal.TerminalSize;
import org.strassburger.tui4j.formatting.PlainTextRenderer;
import org.strassburger.tui4j.formatting.StyledText;
import org.strassburger.tui4j.formatting.StyledTextRenderer;
import org.strassburger.tui4j.formatting.ansi.AnsiCode;
import org.strassburger.tui4j.formatting.ansi.AnsiRenderer;
import org.strassburger.tui4j.formatting.layout.Renderable;

import java.io.*;
import java.util.Scanner;

public class TerminalConsole implements Console {
    private static final int BUFFER_SIZE = 1 << 16; // 64 KB buffer

    private final Terminal terminal;
    private final StyledTextRenderer textRenderer;
    private final Scanner scanner = new Scanner(System.in);
    private final PrintWriter out;

    private boolean autoflush;
    private int batchDepth = 0;

    /**
     * Creates a TerminalConsole that writes to stdout with autoflush enabled,
     * using an AnsiRenderer if the terminal supports ANSI and a PlainTextRenderer otherwise
     */
    public TerminalConsole() {
        this(true);
    }

    /**
     * Creates a TerminalConsole that writes to stdout,
     * using an AnsiRenderer if the terminal supports ANSI and a PlainTextRenderer otherwise
     * @param autoflush whether to flush after every print call
     */
    public TerminalConsole(boolean autoflush) {
        this(new Terminal(), autoflush);
    }

    /**
     * Creates a TerminalConsole that writes to stdout with autoflush enabled
     * @param renderer the StyledTextRenderer to use for rendering styled text
     */
    public TerminalConsole(StyledTextRenderer renderer) {
        this(new Terminal(), renderer);
    }

    /**
     * Creates a TerminalConsole that writes to stdout with autoflush enabled
     * @param terminal the terminal to use for getting the capabilities
     * @param renderer the StyledTextRenderer to use for rendering styled text
     */
    public TerminalConsole(Terminal terminal, StyledTextRenderer renderer) {
        this(terminal, renderer, defaultOutput(), true);
    }

    /**
     * Creates a TerminalConsole with autoflush enabled
     * @param terminal the terminal to use for getting the capabilities
     * @param renderer the StyledTextRenderer to use for rendering styled text
     * @param os the stream to write to
     */
    public TerminalConsole(Terminal terminal, StyledTextRenderer renderer, OutputStream os) {
        this(terminal, renderer, os, true);
    }

    /**
     * Creates a TerminalConsole
     * @param terminal the terminal to use for getting the capabilities
     * @param renderer the StyledTextRenderer to use for rendering styled text
     * @param os the stream to write to
     * @param autoflush whether to flush after every print call
     */
    public TerminalConsole(Terminal terminal, StyledTextRenderer renderer, OutputStream os, boolean autoflush) {
        this.terminal = terminal;
        this.textRenderer = renderer;
        this.out = getPrintWriter(os);
        this.autoflush = autoflush;
    }

    private TerminalConsole(Terminal terminal, boolean autoflush) {
        this(terminal, defaultRenderer(terminal), defaultOutput(), autoflush);
    }

    private static StyledTextRenderer defaultRenderer(Terminal terminal) {
        return terminal.isAnsiSupported() ? new AnsiRenderer() : new PlainTextRenderer();
    }

    private static OutputStream defaultOutput() {
        return new FileOutputStream(FileDescriptor.out);
    }

    private static PrintWriter getPrintWriter(OutputStream os) {
        return new PrintWriter(
                new BufferedWriter(
                        new OutputStreamWriter(os, StdoutCharset.get()), BUFFER_SIZE
                ),
                false
        );
    }

    @Override
    public void flush() {
        out.flush();
    }

    /**
     * Call after each write to flush if autoflush is enabled
     */
    private void autoFlush() {
        if (autoflush && batchDepth == 0) flush();
    }

    @Override
    public String readLine() {
        flush();
        return scanner.nextLine();
    }

    @Override
    public synchronized void print(char c) {
        out.print(c);
        autoFlush();
    }

    @Override
    public synchronized void print(String text) {
        out.print(text);
        autoFlush();
    }

    @Override
    public synchronized void print(StyledText text) {
        out.print(textRenderer.render(text));
        autoFlush();
    }

    @Override
    public synchronized void print(Renderable renderable) {
        TerminalSize.Size size = terminal.getSize();
        batch(() -> renderable.render(this, size.width(), size.height()));
    }

    @Override
    public synchronized void println() {
        out.println();
        autoFlush();
    }

    @Override
    public synchronized void println(char c) {
        out.println(c);
        autoFlush();
    }

    @Override
    public synchronized void println(String text) {
        out.println(text);
        autoFlush();
    }

    @Override
    public synchronized void println(StyledText text) {
        out.println(textRenderer.render(text));
        autoFlush();
    }

    @Override
    public synchronized void println(Renderable renderable) {
        TerminalSize.Size size = terminal.getSize();
        batch(() -> {
            renderable.render(this, size.width(), size.height());
            out.println();
        });
    }

    @Override
    public synchronized void printf(String format, Object... args) {
        out.printf(format, args);
        autoFlush();
    }

    @Override
    public synchronized void printfln(String format, Object... args) {
        out.printf(format + "%n", args);
        autoFlush();
    }

    @Override
    public synchronized void printf(StyledText format, Object... args) {
        out.printf(textRenderer.render(format), args);
        autoFlush();
    }

    @Override
    public synchronized void printfln(StyledText format, Object... args) {
        out.printf(textRenderer.render(format) + "%n", args);
        autoFlush();
    }

    @Override
    public synchronized void clear() {
        if (terminal.isAnsiSupported()) {
            out.println(AnsiCode.ERASE_DISPLAY_ENTIRE + AnsiCode.CURSOR_HOME.toString());
        } else {
            for (int i = 0; i < 50; i++) {
                out.println();
            }
        }
        autoFlush();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final synchronized Batch buffered() {
        batchDepth++;
        boolean[] closed = {false};
        return () -> {
            synchronized (TerminalConsole.this) {
                if (closed[0]) return;
                closed[0] = true;
                if (--batchDepth == 0) flush();
            }
        };
    }

    /**
     * Returns whether this console flushes after every print call.
     * Even when this is {@code true}, nothing is flushed while a {@link #buffered() batch} is open.
     *
     * @return {@code true} if autoflush is enabled
     */
    public synchronized boolean isAutoflush() {
        return this.autoflush;
    }

    /**
     * Enables or disables flushing after every print call.
     *
     * <p>When autoflush is disabled, output is written once the 64 KB buffer is full,
     * when {@link #flush()} is called, when a {@link #buffered() batch} ends, or before
     * {@link #readLine()}. Call {@link #flush()} before the program exits, or the end of
     * the output may be lost.
     *
     * @param autoflush {@code true} to flush after every print call
     */
    public synchronized void setAutoflush(boolean autoflush) {
        this.autoflush = autoflush;
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public StyledTextRenderer getTextRenderer() {
        return textRenderer;
    }
}
