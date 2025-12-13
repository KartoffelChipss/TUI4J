package org.strassburger.tui4j.printer;

import org.strassburger.tui4j.formatting.StyledText;

/**
 * An interface for printing text to various outputs
 */
public interface Printer {
    /**
     * Print raw text to the output
     * @param text the text to print
     */
    void print(String text);

    /**
     * Print styled text to the output
     * @param text the styled text to print
     */
    void print(StyledText text);

    /**
     * Print a newline to the output
     */
    void println();

    /**
     * Print raw text to the output with a newline
     * @param text the text to print
     */
    void println(String text);

    /**
     * Print styled text to the output with a newline
     * @param text the styled text to print
     */
    void println(StyledText text);

    /**
     * Format and print text to the output
     * @param format the format string
     * @param args the arguments to format
     */
    void printf(String format, Object... args);

    /**
     * Format and print text to the output with a newline
     * @param format the format string
     * @param args the arguments to format
     */
    void printfln(String format, Object... args);

    /**
     * Format and print styled text to the output
     * @param format the styled text format
     * @param args the arguments to format
     */
    void printf(StyledText format, Object... args);

    /**
     * Format and print styled text to the output with a newline
     * @param format the styled text format
     * @param args the arguments to format
     */
    void printfln(StyledText format, Object... args);
}
