package org.strassburger.tui4j.printer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.strassburger.tui4j.console.TerminalConsole;
import org.strassburger.tui4j.console.terminal.Terminal;
import org.strassburger.tui4j.formatting.Span;
import org.strassburger.tui4j.formatting.StyledText;

import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TerminalConsoleTest {
    private ByteArrayOutputStream outContent;
    private TerminalConsole console;

    @BeforeEach
    void setUp() {
        outContent = new ByteArrayOutputStream();
        console = new TerminalConsole(
                new Terminal(),
                (StyledText text) -> text.getSpans().stream().map(Span::getText).reduce("", String::concat),
                outContent
        );
    }

    @Test
    void testPrintString() {
        console.print("Hello");
        assertEquals("Hello", outContent.toString());
    }

    @Test
    void testPrintlnString() {
        console.println("Hello");
        assertEquals("Hello" + System.lineSeparator(), outContent.toString());
    }

    @Test
    void testPrintfString() {
        console.printf("Hello %s", "World");
        assertEquals("Hello World", outContent.toString());
    }

    @Test
    void testPrintflnString() {
        console.printfln("Hello %s", "World");
        assertEquals("Hello World" + System.lineSeparator(), outContent.toString());
    }

    @Test
    void testPrintStyledText() {
        StyledText text = StyledText.text("Hello Styled");
        console.print(text);
        assertEquals("Hello Styled", outContent.toString());
    }

    @Test
    void testPrintlnStyledText() {
        StyledText text = StyledText.text("Hello Styled");
        console.println(text);
        assertEquals("Hello Styled" + System.lineSeparator(), outContent.toString());
    }

    @Test
    void testPrintfStyledText() {
        StyledText text = StyledText.text("Hello %s");
        console.printf(text, "World");
        assertEquals("Hello World", outContent.toString());
    }

    @Test
    void testPrintflnStyledText() {
        StyledText text = StyledText.text("Hello %s");
        console.printfln(text, "World");
        assertEquals("Hello World" + System.lineSeparator(), outContent.toString());
    }
}