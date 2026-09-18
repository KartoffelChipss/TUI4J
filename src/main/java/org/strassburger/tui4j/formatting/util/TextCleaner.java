package org.strassburger.tui4j.formatting.util;

/**
 * @deprecated do not use this anymore
 */
@Deprecated
public class TextCleaner {
    public static String stripAnsiCodes(String text) {
        return text.replaceAll("\u001B\\[[;\\d]*m", "");
    }
}
