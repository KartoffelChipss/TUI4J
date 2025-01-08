package org.strassburger.formatting;

/**
 * A class to format and print text to the console
 */
public class Printer {

    /**
     * Print text to the console
     * @param text the text to print
     */
    public static void print(String text) {
        text = TextFormatter.format(text);
        System.out.print(text);
    }

    /**
     * Print text to the console with a newline
     * @param text the text to print
     */
    public static void println(String text) {
        text = TextFormatter.format(text);
        System.out.println(text);
    }

    /**
     * Format and print text to the console with a newline
     * @param format the format string
     * @param args the arguments to format
     */
    public static void printf(String format, Object... args) {
        format = TextFormatter.format(format);
        System.out.printf(format, args);
    }

    /**
     * Format and print text to the console with a newline
     * @param format the format string
     * @param args the arguments to format
     */
    public static void printfln(String format, Object... args) {
        format = TextFormatter.format(format);
        System.out.printf(format + "\n", args);
    }

    /**
     * Print text to the console centered
     * @param text the text to print
     */
    public static void printCentered(String text) {
        System.out.println(TextFormatter.center(text));
    }

    /**
     * Print text to the console with a space between
     * @param leftText the text to print on the left
     * @param rightText the text to print on the right
     */
    public static void printSpaceBetween(String leftText, String rightText) {
        System.out.println(TextFormatter.spaceBetween(leftText, rightText));
    }

    /**
     * Print text to the console with a space between
     * @param leftText the text to print on the left
     * @param rightText the text to print on the right
     * @param spaceChar the character to use for the space
     */
    public static void printSpaceBetween(String leftText, String rightText, String spaceChar) {
        System.out.println(TextFormatter.spaceBetween(leftText, rightText, spaceChar));
    }
}
