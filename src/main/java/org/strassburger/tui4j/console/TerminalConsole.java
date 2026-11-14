package org.strassburger.tui4j.console;

import org.jspecify.annotations.Nullable;
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

/**
 * A {@link Console} that reads from stdin and writes to stdout (or another stream),
 * rendering {@link StyledText} for the terminal.
 *
 * <h2>Getting a console</h2>
 * Most programs should call {@link #system()} once and pass the returned console around:
 * <pre>{@code
 * Console console = TerminalConsole.system();
 * console.println(StyledText.text("Hello"));
 * String name = console.readLine();
 * }</pre>
 * Several consoles on stdin and stdout interfere with each other: they can take each other's
 * input, and their output can appear out of order. The public constructors are meant for
 * tests and for writing to another stream.
 *
 * <h2>Buffering and flushing</h2>
 * Output is collected in a 64 KB buffer. With autoflush enabled (the default), the buffer is
 * flushed after every print call, so output shows up immediately. For large output, use
 * {@link #batch(Runnable)} or {@link #buffered()} to flush only once at the end:
 * <pre>{@code
 * console.batch(() -> {
 *     for (String line : lines) {
 *         console.println(line);
 *     }
 * });
 * }</pre>
 * Printing a {@link Renderable} is batched automatically, and {@link #readLine()} always
 * flushes before reading, so prompts are shown. Autoflush can be turned off with
 * {@link #setAutoflush(boolean)}; output is then written when the buffer is full or on
 * {@link #flush()}. The {@link #system()} console is also flushed when the program exits.
 *
 * <h2>Rendering</h2>
 * By default, styled text is rendered with an {@link AnsiRenderer} if the terminal supports
 * ANSI escape codes and with a {@link PlainTextRenderer} otherwise. A custom
 * {@link StyledTextRenderer} can be passed to the constructors.
 *
 * <h2>Thread safety</h2>
 * The print methods, {@link #buffered()} and the autoflush getter and setter are synchronized
 * on the console, so it can be used from several threads. Each print call and each printed
 * {@link Renderable} is written as a whole, but output from different threads can be
 * interleaved between calls, including inside a batch. {@link #readLine()} is not synchronized.
 *
 * @see Console
 */
public class TerminalConsole implements Console {
    private static @Nullable TerminalConsole instance;

    private static final int BUFFER_SIZE = 1 << 16; // 64 KB buffer

    private final Terminal terminal;
    private final StyledTextRenderer textRenderer;
    private final Scanner scanner = new Scanner(System.in);
    private final PrintWriter out;

    private boolean autoflush;
    private int batchDepth = 0;

    /**
     * Returns the console for this program's stdin and stdout, creating it on the first call.
     * Every later call returns the same instance.
     *
     * <p>Most programs should use this instance and pass it around rather than constructing
     * their own. Each console reads stdin through its own {@link Scanner}, which reads ahead,
     * so two consoles on stdin can take each other's input. Each console also has its own
     * output buffer, so output from two consoles on stdout can appear out of order.
     *
     * <p>The instance uses the default renderer (an {@link AnsiRenderer} if the terminal
     * supports ANSI escape codes, a {@link PlainTextRenderer} otherwise) and starts with
     * autoflush enabled. It registers a shutdown hook that flushes it when the JVM exits
     * normally, so buffered output is not lost. The hook does not run if the JVM is halted
     * with {@link Runtime#halt(int)} or the process is killed.
     *
     * <p>Because the instance is shared, its settings are too: calling
     * {@link #setAutoflush(boolean)} on it changes the behaviour for every part of the program.
     * Code that only needs to print a lot of output at once should use {@link #batch(Runnable)}
     * or {@link #buffered()} instead, which restore the previous behaviour when they end.
     *
     * <p>Tests should not use this instance, since it cannot be reset. Construct a console
     * with {@link #TerminalConsole(Terminal, StyledTextRenderer, OutputStream, boolean)} instead.
     *
     * @return the shared console for stdin and stdout
     */
    public static synchronized TerminalConsole system() {
        if (instance == null) {
            TerminalConsole console = new TerminalConsole();
            Runtime.getRuntime().addShutdownHook(new Thread(console::flush, "tui4j-flush"));
            instance = console;
        }
        return instance;
    }

    /**
     * Creates a new console on stdin and stdout with autoflush enabled, using an
     * {@link AnsiRenderer} if the terminal supports ANSI and a {@link PlainTextRenderer} otherwise.
     *
     * <p>Most programs should use the shared {@link #system()} console instead; see there for
     * why several consoles on stdin and stdout interfere with each other. Unlike {@link #system()},
     * this console is not flushed automatically when the program exits.
     */
    public TerminalConsole() {
        this(true);
    }

    /**
     * Creates a new console on stdin and stdout, using an {@link AnsiRenderer} if the terminal
     * supports ANSI and a {@link PlainTextRenderer} otherwise.
     *
     * <p>Most programs should use the shared {@link #system()} console instead and batch large
     * output with {@link #batch(Runnable)}. This console is not flushed automatically when the
     * program exits, so with autoflush disabled, call {@link #flush()} before exiting.
     *
     * @param autoflush whether to flush after every print call
     */
    public TerminalConsole(boolean autoflush) {
        this(new Terminal(), autoflush);
    }

    /**
     * Creates a new console on stdin and stdout with autoflush enabled and a custom renderer.
     *
     * <p>Only one console should use stdin and stdout; see {@link #system()}. This console is
     * not flushed automatically when the program exits.
     *
     * @param renderer the StyledTextRenderer to use for rendering styled text
     */
    public TerminalConsole(StyledTextRenderer renderer) {
        this(new Terminal(), renderer);
    }

    /**
     * Creates a new console on stdin and stdout with autoflush enabled, a custom renderer
     * and a custom terminal.
     *
     * <p>Only one console should use stdin and stdout; see {@link #system()}. This console is
     * not flushed automatically when the program exits.
     *
     * @param terminal the terminal to use for getting the capabilities
     * @param renderer the StyledTextRenderer to use for rendering styled text
     */
    public TerminalConsole(Terminal terminal, StyledTextRenderer renderer) {
        this(terminal, renderer, defaultOutput(), true);
    }

    /**
     * Creates a new console that reads stdin, writes to the given stream and has autoflush enabled.
     *
     * <p>Useful for tests and for writing to something other than stdout.
     * This console is not flushed automatically when the program exits.
     *
     * @param terminal the terminal to use for getting the capabilities
     * @param renderer the StyledTextRenderer to use for rendering styled text
     * @param os the stream to write to, encoded with the stdout charset
     */
    public TerminalConsole(Terminal terminal, StyledTextRenderer renderer, OutputStream os) {
        this(terminal, renderer, os, true);
    }

    /**
     * Creates a new console that reads stdin and writes to the given stream.
     *
     * <p>Useful for tests and for writing to something other than stdout. Input is always read
     * from {@link System#in} as it is when the console is created. This console is not flushed
     * automatically when the program exits.
     *
     * @param terminal the terminal to use for getting the capabilities
     * @param renderer the StyledTextRenderer to use for rendering styled text
     * @param os the stream to write to, encoded with the stdout charset
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

    /**
     * Writes all buffered output to the underlying stream.
     *
     * <p>Only needed when autoflush is disabled and no batch is used. The {@link #system()}
     * console is also flushed when the program exits; other consoles are not.
     */
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

    /**
     * {@inheritDoc}
     *
     * <p>Flushes first, so a prompt printed just before is shown even with autoflush disabled
     * or inside a batch. Input is read from {@link System#in} as it was when this console was
     * created. Several consoles on stdin can take each other's input; use {@link #system()}.
     *
     * @throws java.util.NoSuchElementException if stdin has no more lines
     */
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
     * {@link #readLine()}. The {@link #system()} console is flushed when the program exits;
     * on any other console, call {@link #flush()} before exiting, or the end of the output
     * may be lost.
     *
     * <p>On the shared {@link #system()} console this setting affects the whole program.
     * To speed up one large piece of output, use {@link #batch(Runnable)} or
     * {@link #buffered()} instead, which leave the setting unchanged.
     *
     * @param autoflush {@code true} to flush after every print call
     */
    public synchronized void setAutoflush(boolean autoflush) {
        this.autoflush = autoflush;
    }

    /**
     * Returns the terminal this console uses to detect ANSI support and the terminal size.
     *
     * @return the terminal of this console
     */
    public Terminal getTerminal() {
        return terminal;
    }

    /**
     * Returns the renderer this console uses to turn {@link StyledText} into a string.
     *
     * @return the renderer of this console
     */
    public StyledTextRenderer getTextRenderer() {
        return textRenderer;
    }
}
