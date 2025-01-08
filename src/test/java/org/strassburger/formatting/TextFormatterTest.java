package org.strassburger.formatting;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TextFormatterTest {

    private final int terminalWidth = 80;

    @Test
    void testFormatWithColorCodes() {
        String input = "&1Blue &2Green &lBold";
        String formattedText = TextFormatter.format(input);

        assertTrue(formattedText.contains(TextColor.BLUE.toString()));
        assertTrue(formattedText.contains(TextColor.GREEN.toString()));
        assertTrue(formattedText.contains(TextColor.BOLD.toString()));
    }

    @Test
    void testFormatWithEscapeCharacter() {
        String input = "\\&1Blue &2Green";
        String formattedText = TextFormatter.format(input);

        assertTrue(formattedText.contains("&1Blue"));
        assertTrue(formattedText.contains(TextColor.GREEN.toString()));
    }

    @Test
    void testCenterText() {
        String input = "CenteredText";
        String centeredText = TextFormatter.center(input, terminalWidth);

        int spacesBefore = centeredText.indexOf(input);
        int spacesAfter = terminalWidth - (spacesBefore + input.length());

        assertEquals(spacesBefore, spacesAfter, "Text should be centered.");
    }

    @Test
    void testSpaceBetween() {
        String text1 = "&aLeft";
        String text1Raw = "Left";
        String text2 = "&aRight";
        String text2Raw = "Right";
        String result = TextFormatter.spaceBetween(text1, text2, " ", terminalWidth);

        assertTrue(result.contains(text1Raw), "The result text should contain the first text.");
        assertTrue(result.contains(text2Raw), "The result text should contain the second text.");

        int resultLength = result.replaceAll("\u001B\\[[;\\d]*m", "").length();
        assertEquals(terminalWidth, resultLength, "The result text should fit the terminal width.");
    }

    @Test
    void testSpaceBetweenWithCustomSpaceChar() {
        String text1 = "Left";
        String text2 = "Right";
        String spaceChar = "*";
        String result = TextFormatter.spaceBetween(text1, text2, spaceChar, terminalWidth);

        assertTrue(result.contains(text1));
        assertTrue(result.contains(text2));
        assertTrue(result.contains(spaceChar));
    }

    @Test
    void testSpaceBetweenWithCustomTerminalWidth() {
        String text1 = "&aLeft";
        String text1Raw = "Left";
        String text2 = "&aRight";
        String text2Raw = "Right";
        int customTerminalWidth = 100;
        String result = TextFormatter.spaceBetween(text1, text2, " ", customTerminalWidth);

        assertTrue(result.contains(text1Raw), "The result text should contain the first text.");
        assertTrue(result.contains(text2Raw), "The result text should contain the second text.");

        int resultLength = result.replaceAll("\u001B\\[[;\\d]*m", "").length();
        assertEquals(customTerminalWidth, resultLength, "The result text should fit the custom terminal width.");
    }
}