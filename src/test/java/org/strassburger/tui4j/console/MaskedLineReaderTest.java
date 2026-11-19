package org.strassburger.tui4j.console;

import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class MaskedLineReaderTest {

    private final StringWriter echoed = new StringWriter();
    private final PrintWriter out = new PrintWriter(echoed);

    private String read(String keys) {
        return MaskedLineReader.read(new StringReader(keys), out, '*');
    }

    @Test
    void readsLineAndEchoesMask() {
        assertEquals("secret", read("secret\nrest"));
        assertEquals("******" + System.lineSeparator(), echoed.toString());
    }

    @Test
    void stopsAtCarriageReturn() {
        assertEquals("pw", read("pw\r"));
    }

    @Test
    void backspaceRemovesLastCharacter() {
        assertEquals("pas", read("pass\u007F\n"));
        assertEquals("****\b \b" + System.lineSeparator(), echoed.toString());
    }

    @Test
    void backspaceOnEmptyLineDoesNothing() {
        assertEquals("a", read("\b\ba\n"));
        assertEquals("*" + System.lineSeparator(), echoed.toString());
    }

    @Test
    void ctrlUClearsLine() {
        assertEquals("b", read("aaa\u0015b\n"));
    }

    @Test
    void ignoresEscapeSequencesAndControlCharacters() {
        assertEquals("ab", read("a\u001B[D\u001BOB\u001B[1;5C\tb\n"));
    }

    @Test
    void masksSurrogatePairOnceAndDeletesItWhole() {
        assertEquals("a", read("a😀\u007F\n"));
        assertEquals("**\b \b" + System.lineSeparator(), echoed.toString());
    }

    @Test
    void returnsLineAtEndOfInput() {
        assertEquals("abc", read("abc"));
    }

    @Test
    void throwsAtEndOfInputWithoutLine() {
        assertThrows(NoSuchElementException.class, () -> read(""));
    }
}
