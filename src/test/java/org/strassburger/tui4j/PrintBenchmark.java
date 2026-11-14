package org.strassburger.tui4j;

import org.strassburger.tui4j.console.TerminalConsole;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class PrintBenchmark {
    private static final int LINES = 2_000_000;

    private record Result(String name, long ms) {}

    private static long timeMs(Runnable body) {
        long start = System.nanoTime();
        body.run();
        return (System.nanoTime() - start) / 1_000_000;
    }

    public static void main(String[] args) {
        List<Result> results = new ArrayList<>();

        // 1) Plain System.out.println
        results.add(new Result("System.out.println", timeMs(() -> {
            for (int i = 0; i < LINES; i++) {
                System.out.println("Line number " + i);
            }
        })));

        // 2) Hand-built buffered PrintWriter
        results.add(new Result("Buffered PrintWriter", timeMs(() -> {
            PrintWriter out = new PrintWriter(
                    new BufferedWriter(new OutputStreamWriter(System.out), 1 << 16));
            for (int i = 0; i < LINES; i++) {
                out.println("Line number " + i);
            }
            out.flush();
        })));

        // 3) TerminalConsole with autoflush = true.
        results.add(new Result("TerminalConsole (autoflush=true)", timeMs(() -> {
            TerminalConsole console = new TerminalConsole(true);
            for (int i = 0; i < LINES; i++) {
                console.println("Line number " + i);
            }
            console.flush();
        })));

        // 4) TerminalConsole with autoflush = false, flush once at the end.
        results.add(new Result("TerminalConsole (autoflush=false)", timeMs(() -> {
            TerminalConsole console = new TerminalConsole(false);
            for (int i = 0; i < LINES; i++) {
                console.println("Line number " + i);
            }
            console.flush();
        })));

        long baseline = results.get(0).ms();
        System.err.printf("%-36s %8s %9s%n", "Variant", "Time", "Speedup");
        for (Result r : results) {
            System.err.printf("%-36s %6d ms %8.1fx%n", r.name(), r.ms(), (double) baseline / r.ms());
        }
    }
}