package org.strassburger.tui4j.terminal;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Utility class to get the terminal size.
 */
public final class TerminalSize {

    private TerminalSize() {
    }

    /**
     * Attempts to get the terminal size using multiple methods.<br/>
     * Falls back to a default size of 80x24 if all methods fail.
     *
     * @return the terminal Size
     */
    public static Size get() {
        Size size = fromStty();
        if (size != null) return size;

        size = fromWindowsMode();
        if (size != null) return size;

        size = fromEnv();
        if (size != null) return size;

        size = fromTput();
        if (size != null) return size;

        return DEFAULT_SIZE;
    }

    /* Unix: stty */
    private static Size fromStty() {
        try {
            Process p = new ProcessBuilder("sh", "-c", "stty size < /dev/tty")
                    .redirectErrorStream(true)
                    .start();

            try (BufferedReader r = new BufferedReader(
                    new InputStreamReader(p.getInputStream()))) {

                String line = r.readLine();
                if (line != null) {
                    String[] parts = line.trim().split("\\s+");
                    if (parts.length == 2) {
                        int rows = Integer.parseInt(parts[0]);
                        int cols = Integer.parseInt(parts[1]);
                        return new Size(cols, rows);
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    /* Windows: mode con */
    private static Size fromWindowsMode() {
        if (!isWindows()) return null;

        try {
            Process p = new ProcessBuilder("cmd", "/c", "mode con")
                    .redirectErrorStream(true)
                    .start();

            try (BufferedReader r = new BufferedReader(
                    new InputStreamReader(p.getInputStream()))) {

                String line;
                Integer cols = null, rows = null;

                while ((line = r.readLine()) != null) {
                    line = line.trim();
                    if (line.startsWith("Columns:")) {
                        cols = Integer.parseInt(line.replaceAll("\\D+", ""));
                    } else if (line.startsWith("Lines:")) {
                        rows = Integer.parseInt(line.replaceAll("\\D+", ""));
                    }
                }

                if (cols != null && rows != null) {
                    return new Size(cols, rows);
                }
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    /* Environment variables */
    private static Size fromEnv() {
        Map<String, String> env = System.getenv();
        try {
            String cols = env.get("COLUMNS");
            String rows = env.get("LINES");
            if (cols != null && rows != null) {
                return new Size(Integer.parseInt(cols), Integer.parseInt(rows));
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    /* Unix: tput */
    private static Size fromTput() {
        try {
            int cols = runAndReadInt("tput", "cols");
            int rows = runAndReadInt("tput", "lines");
            if (cols > 0 && rows > 0) {
                return new Size(cols, rows);
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    private static int runAndReadInt(String... cmd) throws Exception {
        Process p = new ProcessBuilder(cmd)
                .redirectErrorStream(true)
                .start();

        try (BufferedReader r = new BufferedReader(
                new InputStreamReader(p.getInputStream()))) {

            String line = r.readLine();
            return line == null ? -1 : Integer.parseInt(line.trim());
        }
    }

    private static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    /**
     * Represents the size of a terminal in columns and rows.
     */
    public record Size(int width, int height) {
        @Override
        public String toString() {
            return width + "x" + height;
        }
    }

    /**
     * Default terminal size if detection fails (80 columns x 24 rows)
     */
    private static final Size DEFAULT_SIZE = new Size(80, 24);
}
