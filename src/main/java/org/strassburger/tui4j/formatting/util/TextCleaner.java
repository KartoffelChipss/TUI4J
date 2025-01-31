package org.strassburger.tui4j.formatting.util;

public class TextCleaner {
    public static String stripAnsiCodes(String text) {
        return text.replaceAll("\u001B\\[[;\\d]*m", "");
    }
}
