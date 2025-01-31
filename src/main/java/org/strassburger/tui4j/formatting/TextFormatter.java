package org.strassburger.tui4j.formatting;

import org.strassburger.colorlab4j.color.Color;
import org.strassburger.colorlab4j.color.spaces.HSLColor;
import org.strassburger.colorlab4j.gradients.Gradient;
import org.strassburger.colorlab4j.gradients.spaces.HSLGradient;
import org.strassburger.tui4j.formatting.util.ColorCodeReplacer;
import org.strassburger.tui4j.formatting.util.GradientFormatter;
import org.strassburger.tui4j.formatting.util.TextCleaner;

/**
 * Format text with color codes and position text in the terminal
 */
public class TextFormatter {
    private static int getTerminalWidth() {
        return 80;
    }

    /**
     * Format text with color codes
     * @param text Text with color codes
     * @return Formatted text
     */
    public static String format(String text) {
        text = ColorCodeReplacer.replacePrefabColorCodes(text);
        text = ColorCodeReplacer.replaceHexCodes(text);
        text = ColorCodeReplacer.replaceBgHexCodes(text);
        return text;
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

        return " ".repeat(Math.max(0, spaces)) + text;
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

        int textLength = text1.replaceAll("\u001B\\[[;\\d]*m", "").length()
                + text2.replaceAll("\u001B\\[[;\\d]*m", "").length();

        int spaces = terminalWidth - textLength;

        return text1 + spaceChar.repeat(Math.max(0, spaces)) + text2;
    }

    /**
     * Clear formatting from text
     * @param text Text with color codes
     * @return Text without color codes
     */
    public static String clearFormatting(String text) {
        return TextCleaner.stripAnsiCodes(text);
    }

    /**
     * Add an HSL gradient to text
     * @param text Text to add gradient to
     * @param startColor Start color of the gradient
     * @param endColor End color of the gradient
     * @return The text formatted with an HSL gradient
     */
    public static String gradient(String text, String startColor, String endColor) {
        return gradient(text, HSLColor.fromHex(startColor), HSLColor.fromHex(endColor));
    }

    /**
     * Add an HSL gradient to text
     * @param text Text to add gradient to
     * @param startColor Start color of the gradient
     * @param endColor End color of the gradient
     * @return The text formatted with an HSL gradient
     */
    public static <T extends Color> String gradient(String text, T startColor, T endColor) {
        HSLColor startColorHSL = startColor.toHSL();
        HSLColor endColorHSL = endColor.toHSL();

        HSLGradient gradient = new HSLGradient(startColorHSL, endColorHSL);
        return gradient(text, gradient);
    }

    /**
     * Add a gradient to text
     * @param text Text to add gradient to
     * @param gradient Gradient to apply
     * @return The text formatted with a gradient
     */
    public static <T extends Color, U extends Gradient<T>> String gradient(String text, U gradient) {
        return GradientFormatter.applyGradient(text, gradient);
    }
}
