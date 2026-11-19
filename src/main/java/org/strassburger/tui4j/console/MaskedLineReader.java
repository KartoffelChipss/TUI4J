package org.strassburger.tui4j.console;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.NoSuchElementException;

/**
 * Reads a line key by key from a terminal in non-canonical mode, echoing a mask character
 * for every typed character. The terminal must already have echo and line buffering disabled.
 */
final class MaskedLineReader {
    private static final int ESC = 27;
    private static final int DEL = 127;
    private static final int CTRL_U = 21;

    private MaskedLineReader() {
    }

    /**
     * Reads characters until Enter or end of input. Backspace removes the last character,
     * Ctrl+U clears the line, escape sequences (such as arrow keys) and other control
     * characters are ignored.
     *
     * @param in the reader to read keys from
     * @param out the writer to echo the mask to; it is flushed after every change
     * @param mask the character echoed for every typed character
     * @return the typed line
     * @throws NoSuchElementException if the input ends before anything was typed
     */
    static String read(Reader in, PrintWriter out, char mask) {
        StringBuilder sb = new StringBuilder();
        int shown = 0;
        try {
            while (true) {
                int c = in.read();
                if (c == -1) {
                    if (sb.isEmpty()) throw new NoSuchElementException("No line found");
                    break;
                }
                if (c == '\n' || c == '\r') break;

                if (c == DEL || c == '\b') {
                    if (!sb.isEmpty()) {
                        sb.setLength(sb.offsetByCodePoints(sb.length(), -1));
                        out.print("\b \b");
                        shown--;
                        out.flush();
                    }
                    continue;
                }
                if (c == CTRL_U) {
                    sb.setLength(0);
                    out.print("\b \b".repeat(shown));
                    shown = 0;
                    out.flush();
                    continue;
                }
                if (c == ESC) {
                    skipEscapeSequence(in);
                    continue;
                }
                if (c < 32) continue;

                sb.append((char) c);
                // print one mask per code point, not per UTF-16 unit
                if (!Character.isHighSurrogate((char) c)) {
                    out.print(mask);
                    shown++;
                    out.flush();
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        out.println();
        out.flush();
        return sb.toString();
    }

    private static void skipEscapeSequence(Reader in) throws IOException {
        int c = in.read();
        if (c == 'O') {
            in.read();
        } else if (c == '[') {
            do {
                c = in.read();
            } while (c != -1 && (c < 0x40 || c > 0x7E));
        }
    }
}
