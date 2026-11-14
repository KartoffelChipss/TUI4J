package org.strassburger.tui4j.printer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.strassburger.tui4j.console.Batch;
import org.strassburger.tui4j.console.TerminalConsole;
import org.strassburger.tui4j.console.terminal.Terminal;
import org.strassburger.tui4j.formatting.Span;
import org.strassburger.tui4j.formatting.StyledText;
import org.strassburger.tui4j.formatting.layout.Renderable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests autoflush and the batch API of {@link TerminalConsole}.
 *
 * <p>The console buffers up to 64 KB, so nothing reaches {@link #out} until it is flushed.
 * That makes "nothing written yet" and "flushed" directly observable.
 */
class TerminalConsoleBufferingTest {
    private static final String NL = System.lineSeparator();

    private final FlushCountingStream out = new FlushCountingStream();
    private final InputStream originalIn = System.in;

    @AfterEach
    void restoreStdin() {
        System.setIn(originalIn);
    }

    private TerminalConsole console(boolean autoflush) {
        return new TerminalConsole(
                new Terminal(),
                (StyledText text) -> text.getSpans().stream().map(Span::getText).reduce("", String::concat),
                out,
                autoflush
        );
    }

    // --- autoflush ---

    @Test
    void autoflushIsEnabledByDefault() {
        TerminalConsole console = new TerminalConsole(new Terminal(), text -> "", out);
        assertTrue(console.isAutoflush());
    }

    @Test
    void autoflushWritesEveryCallImmediately() {
        TerminalConsole console = console(true);

        console.print("a");
        assertEquals("a", out.text());
        console.println("b");
        assertEquals("ab" + NL, out.text());
        assertEquals(2, out.flushes);
    }

    @Test
    void withoutAutoflushNothingIsWrittenUntilFlush() {
        TerminalConsole console = console(false);

        console.print("a");
        console.println("b");
        assertEquals("", out.text());
        assertEquals(0, out.flushes);

        console.flush();
        assertEquals("ab" + NL, out.text());
    }

    @Test
    void setAutoflushTakesEffectForLaterCalls() {
        TerminalConsole console = console(false);
        console.print("a");

        console.setAutoflush(true);
        assertTrue(console.isAutoflush());
        console.print("b");
        assertEquals("ab", out.text());

        console.setAutoflush(false);
        console.print("c");
        assertEquals("ab", out.text());
    }

    @Test
    void readLineFlushesPendingPromptEvenWithoutAutoflush() {
        System.setIn(new ByteArrayInputStream(("answer" + NL).getBytes(StandardCharsets.UTF_8)));
        TerminalConsole console = console(false); // the Scanner reads System.in as of construction

        console.print("Name: ");
        assertEquals("", out.text());

        assertEquals("answer", console.readLine());
        assertEquals("Name: ", out.text());
    }

    // --- batch ---

    @Test
    void batchDefersOutputAndFlushesOnceAtTheEnd() {
        TerminalConsole console = console(true);

        console.batch(() -> {
            for (int i = 0; i < 100; i++) console.println("line " + i);
            assertEquals("", out.text(), "nothing should be flushed inside the batch");
        });

        assertEquals(1, out.flushes);
        assertTrue(out.text().startsWith("line 0" + NL));
        assertTrue(out.text().endsWith("line 99" + NL));
    }

    @Test
    void bufferedDefersOutputUntilClosed() {
        TerminalConsole console = console(true);

        try (Batch ignored = console.buffered()) {
            console.print("a");
            console.print("b");
            assertEquals("", out.text());
        }

        assertEquals("ab", out.text());
        assertEquals(1, out.flushes);
    }

    @Test
    void batchFlushesAtTheEndEvenWithoutAutoflush() {
        TerminalConsole console = console(false);

        console.batch(() -> console.print("a"));

        assertEquals("a", out.text());
    }

    @Test
    void autoflushResumesAfterBatch() {
        TerminalConsole console = console(true);

        console.batch(() -> console.print("a"));
        console.print("b");

        assertEquals("ab", out.text());
        assertEquals(2, out.flushes);
    }

    @Test
    void nestedBatchesOnlyFlushWhenTheOutermostCloses() {
        TerminalConsole console = console(true);

        try (Batch outer = console.buffered()) {
            console.print("a");
            try (Batch inner = console.buffered()) {
                console.print("b");
            }
            assertEquals("", out.text(), "closing the inner batch must not flush");
            console.print("c");
            assertEquals("", out.text(), "autoflush must stay paused while the outer batch is open");
        }

        assertEquals("abc", out.text());
        assertEquals(1, out.flushes);
    }

    @Test
    void closingABatchTwiceHasNoEffect() {
        TerminalConsole console = console(true);

        Batch outer = console.buffered();
        Batch inner = console.buffered();
        inner.close();
        inner.close(); // must not close the outer batch as well

        console.print("a");
        assertEquals("", out.text());

        outer.close();
        assertEquals("a", out.text());
    }

    @Test
    void batchFlushesWhenTheBodyThrows() {
        TerminalConsole console = console(true);

        assertThrows(IllegalStateException.class, () -> console.batch(() -> {
            console.print("before failure");
            throw new IllegalStateException("boom");
        }));

        assertEquals("before failure", out.text());

        console.print("!");
        assertEquals("before failure!", out.text(), "autoflush must resume after a failed batch");
    }

    @Test
    void readLineFlushesInsideABatch() {
        System.setIn(new ByteArrayInputStream(("42" + NL).getBytes(StandardCharsets.UTF_8)));
        TerminalConsole console = console(true);

        console.batch(() -> {
            console.print("Age: ");
            assertEquals("42", console.readLine());
            assertEquals("Age: ", out.text());
        });
    }

    // --- renderables ---

    private static final Renderable THREE_PIECES = (printer, width, height) -> {
        printer.print("x");
        printer.print("y");
        printer.print("z");
    };

    @Test
    void printRenderableFlushesOnce() {
        TerminalConsole console = console(true);

        console.print(THREE_PIECES);

        assertEquals("xyz", out.text());
        assertEquals(1, out.flushes);
    }

    @Test
    void printlnRenderableFlushesOnce() {
        TerminalConsole console = console(true);

        console.println(THREE_PIECES);

        assertEquals("xyz" + NL, out.text());
        assertEquals(1, out.flushes);
    }

    @Test
    void renderableInsideABatchDoesNotFlushEarly() {
        TerminalConsole console = console(true);

        console.batch(() -> {
            console.print(THREE_PIECES);
            assertEquals("", out.text());
        });

        assertEquals("xyz", out.text());
        assertEquals(1, out.flushes);
    }

    // --- threads ---

    @Test
    void concurrentBatchesLeaveTheConsoleConsistent() throws Exception {
        TerminalConsole console = console(true);
        int threads = 8;
        int batchesPerThread = 2_000;

        ExecutorService pool = Executors.newFixedThreadPool(threads);
        CountDownLatch start = new CountDownLatch(1);
        List<Future<?>> futures = new ArrayList<>();
        try {
            for (int t = 0; t < threads; t++) {
                futures.add(pool.submit(() -> {
                    start.await();
                    for (int i = 0; i < batchesPerThread; i++) {
                        console.batch(() -> console.print("."));
                    }
                    return null;
                }));
            }
            start.countDown();
            for (Future<?> f : futures) f.get(30, TimeUnit.SECONDS);
        } finally {
            pool.shutdownNow();
        }

        assertEquals(threads * batchesPerThread, out.text().length(), "every print must be written");

        // If an update to the batch counter was lost, it would stay above zero and autoflush would stop working.
        out.reset();
        console.print("after");
        assertEquals("after", out.text());
    }

    /** Collects written bytes and counts how often it is flushed. */
    private static final class FlushCountingStream extends ByteArrayOutputStream {
        int flushes = 0;

        @Override
        public void flush() {
            flushes++;
        }

        String text() {
            return toString(StandardCharsets.UTF_8);
        }
    }
}
