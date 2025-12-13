package org.strassburger.tui4j.printer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.strassburger.tui4j.formatting.Span;
import org.strassburger.tui4j.formatting.StyledText;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConsolePrinterTest {
    private ByteArrayOutputStream outContent;
    private ConsolePrinter printer;

    @BeforeEach
    void setUp() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        printer = new ConsolePrinter((StyledText text) ->
                text.getSpans().stream().map(Span::getText).reduce("", String::concat));
    }

    @Test
    void testPrintString() {
        printer.print("Hello");
        assertEquals("Hello", outContent.toString());
    }

    @Test
    void testPrintlnString() {
        printer.println("Hello");
        assertEquals("Hello" + System.lineSeparator(), outContent.toString());
    }

    @Test
    void testPrintfString() {
        printer.printf("Hello %s", "World");
        assertEquals("Hello World", outContent.toString());
    }

    @Test
    void testPrintflnString() {
        printer.printfln("Hello %s", "World");
        assertEquals("Hello World" + System.lineSeparator(), outContent.toString());
    }

    @Test
    void testPrintStyledText() {
        StyledText text = StyledText.text("Hello Styled");
        printer.print(text);
        assertEquals("Hello Styled", outContent.toString());
    }

    @Test
    void testPrintlnStyledText() {
        StyledText text = StyledText.text("Hello Styled");
        printer.println(text);
        assertEquals("Hello Styled" + System.lineSeparator(), outContent.toString());
    }

    @Test
    void testPrintfStyledText() {
        StyledText text = StyledText.text("Hello %s");
        printer.printf(text, "World");
        assertEquals("Hello World", outContent.toString());
    }

    @Test
    void testPrintflnStyledText() {
        StyledText text = StyledText.text("Hello %s");
        printer.printfln(text, "World");
        assertEquals("Hello World" + System.lineSeparator(), outContent.toString());
    }
}