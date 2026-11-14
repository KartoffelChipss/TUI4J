package org.strassburger.tui4j.console;

import org.strassburger.tui4j.input.Input;
import org.strassburger.tui4j.input.exceptions.InputValidationException;

public interface Console extends Printer {
    String readLine();

    default <T> T read(Input<T, ?> input) throws InputValidationException {
        return input.read(this);
    }

    /**
     * Starts a batch: autoflush is paused until the returned {@link Batch} is closed,
     * and the console is flushed once at that point. Use this to print large output
     * without flushing after every call.
     *
     * <p>Batches can be nested. Only closing the outermost batch flushes, so a batch
     * opened inside another one, for example by printing a
     * {@link org.strassburger.tui4j.formatting.layout.Renderable}, does not flush early.
     *
     * <p>The flush at the end happens even if autoflush is disabled, so batching is also
     * a way to make sure buffered output is written. {@link #readLine()} still flushes
     * before it reads, so a prompt printed inside a batch is shown.
     *
     * <p>The pause applies to the whole console, not just the calling thread: while a batch
     * is open, prints from other threads are not flushed either. A batch is not atomic;
     * output from other threads can appear between the calls inside it.
     *
     * <p>Prefer this method over {@link #batch(Runnable)} when the body throws checked
     * exceptions, returns early or assigns local variables:
     * <pre>{@code
     * try (Batch ignored = console.buffered()) {
     *     for (String line : Files.readAllLines(path)) {
     *         console.println(line);
     *     }
     * }
     * }</pre>
     *
     * <p>Implementations that do not buffer may return a handle whose {@code close()}
     * only flushes or does nothing.
     *
     * @return a handle that ends the batch when closed; close it with try-with-resources
     * @see #batch(Runnable)
     */
    Batch buffered();

    /**
     * Runs {@code body} as a single batch: autoflush is paused while it runs and the console
     * is flushed once afterwards, even if {@code body} throws.
     *
     * <pre>{@code
     * console.batch(() -> {
     *     for (Anime anime : animeList) {
     *         console.println(anime.title());
     *     }
     * });
     * }</pre>
     *
     * <p>This is a shorthand for {@link #buffered()} used with try-with-resources; nesting and
     * threading behave the same way.
     *
     * @param body the code whose output is batched
     * @see #buffered()
     */
    default void batch(Runnable body) {
        try (Batch ignored = buffered()) {
            body.run();
        }
    }
}
