package org.strassburger.tui4j.formatting;

import java.util.HashMap;
import java.util.Map;

/**
 * Format text with color codes and position text in the terminal
 */
public class TextFormatter {
    private static final Map<String, TextColor> colorMap = new HashMap<>();

    static {
        colorMap.put("&0", TextColor.BLACK);
        colorMap.put("&1", TextColor.BLUE);
        colorMap.put("&2", TextColor.GREEN);
        colorMap.put("&3", TextColor.CYAN);
        colorMap.put("&4", TextColor.RED);
        colorMap.put("&5", TextColor.MAGENTA);
        colorMap.put("&6", TextColor.YELLOW);
        colorMap.put("&7", TextColor.WHITE);

        colorMap.put("&8", TextColor.BRIGHT_BLACK);
        colorMap.put("&9", TextColor.BRIGHT_BLUE);
        colorMap.put("&a", TextColor.BRIGHT_GREEN);
        colorMap.put("&b", TextColor.BRIGHT_CYAN);
        colorMap.put("&c", TextColor.BRIGHT_RED);
        colorMap.put("&d", TextColor.BRIGHT_MAGENTA);
        colorMap.put("&e", TextColor.BRIGHT_YELLOW);
        colorMap.put("&f", TextColor.BRIGHT_WHITE);

        colorMap.put("&l", TextColor.BOLD);
        colorMap.put("&n", TextColor.UNDERLINE);
        colorMap.put("&r", TextColor.RESET);

        colorMap.put("&x0", TextColor.BG_BLACK);
        colorMap.put("&x1", TextColor.BG_BLUE);
        colorMap.put("&x2", TextColor.BG_GREEN);
        colorMap.put("&x3", TextColor.BG_CYAN);
        colorMap.put("&x4", TextColor.BG_RED);
        colorMap.put("&x5", TextColor.BG_MAGENTA);
        colorMap.put("&x6", TextColor.BG_YELLOW);
        colorMap.put("&x7", TextColor.BG_WHITE);

        colorMap.put("&x8", TextColor.BG_BRIGHT_BLACK);
        colorMap.put("&x9", TextColor.BG_BRIGHT_BLUE);
        colorMap.put("&xa", TextColor.BG_BRIGHT_GREEN);
        colorMap.put("&xb", TextColor.BG_BRIGHT_CYAN);
        colorMap.put("&xc", TextColor.BG_BRIGHT_RED);
        colorMap.put("&xd", TextColor.BG_BRIGHT_MAGENTA);
        colorMap.put("&xe", TextColor.BG_BRIGHT_YELLOW);
        colorMap.put("&xf", TextColor.BG_BRIGHT_WHITE);
    }

    /**
     * Format text with color codes
     * @param text Text with color codes
     * @return Formatted text
     */
    public static String format(String text) {
        for (Map.Entry<String, TextColor> entry : colorMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().toString();

            text = text.replaceAll("(?<!\\\\)" + key, value);// Replace non-escaped key
            text = text.replace("\\" + key, key);// Remove escape character
        }
        text += TextColor.RESET;
        return text;
    }

    private static int getTerminalWidth() {
        return 80;
    }

    /**
     * Center text in the terminal
     * @param text Text to center (allows color codes)
     * @return Centered text
     */
    public static String center(String text) {
        return center(text, getTerminalWidth());
    }

    /**
     * Center text in the terminal
     * @param text Text to center (allows color codes)
     * @param terminalWidth Width of the terminal
     * @return Centered text
     */
    public static String center(String text, int terminalWidth) {
        text = format(text);

        int textLength = text.replaceAll("\u001B\\[[;\\d]*m", "").length();

        int spaces = (terminalWidth - textLength) / 2;

        return " ".repeat(Math.max(0, spaces)) +
                text;
    }

    /**
     * Add spaces between two texts
     * @param text1 First text (allows color codes)
     * @param text2 Second text (allows color codes)
     * @return Text with spaces between
     */
    public static String spaceBetween(String text1, String text2) {
        return spaceBetween(text1, text2, " ", getTerminalWidth());
    }

    /**
     * Add spaces between two texts
     * @param text1 First text (allows color codes)
     * @param text2 Second text (allows color codes)
     * @param spaceChar Character to use for spaces (allows color codes)
     * @return Text with spaces between
     */
    public static String spaceBetween(String text1, String text2, String spaceChar) {
        return spaceBetween(text1, text2, spaceChar, getTerminalWidth());
    }

    /**
     * Add spaces between two texts
     * Add spaces between two texts
     * @param text1 First text (allows color codes)
     * @param text2 Second text (allows color codes)
     * @param spaceChar Character to use for spaces (allows color codes)
     * @param terminalWidth Width of the terminal
     * @return Text with spaces between
     */
    public static String spaceBetween(String text1, String text2, String spaceChar, int terminalWidth) {
        text1 = format(text1);
        text2 = format(text2);
        spaceChar = format(spaceChar);

        int textLength = text1.replaceAll("\u001B\\[[;\\d]*m", "").length() + text2.replaceAll("\u001B\\[[;\\d]*m", "").length();

        int spaces = terminalWidth - textLength;

        return text1 +
                spaceChar.repeat(Math.max(0, spaces)) +
                text2;
    }

    /**
     * Clear formatting from text
     * @param text Text with color codes
     * @return Text without color codes
     */
    public static String clearFormatting(String text) {
        return text.replaceAll("\u001B\\[[;\\d]*m", "");
    }
}
