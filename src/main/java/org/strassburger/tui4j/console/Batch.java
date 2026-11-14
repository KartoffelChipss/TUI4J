package org.strassburger.tui4j.console;

/**
 * A handle returned by {@link Console#buffered()} that keeps autoflush paused while it is open.
 * Closing it ends the batch.
 *
 * <p>Use it with try-with-resources so the batch also ends if an exception is thrown:
 * <pre>{@code
 * try (Batch ignored = console.buffered()) {
 *     for (String line : lines) {
 *         console.println(line);
 *     }
 * } // flushed here
 * }</pre>
 *
 * <p>{@link #close()} declares no checked exception, so no {@code catch} block is needed.
 *
 * @see Console#buffered()
 * @see Console#batch(Runnable)
 */
@FunctionalInterface
public interface Batch extends AutoCloseable {
    /**
     * Ends this batch. If it is the outermost open batch, the console is flushed.
     *
     * <p>Closing a batch more than once has no effect after the first call.
     */
    @Override
    void close();
}
