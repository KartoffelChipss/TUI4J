package org.strassburger.tui4j.formatting;

/**
 * This enum provides ANSI escape codes for text colors, background colors, and text styles.
 */
public enum TextColor {
    // Text Colors
    RESET("\033[0m"),
    BLACK("\033[0;30m"),
    RED("\033[0;31m"),
    GREEN("\033[0;32m"),
    YELLOW("\033[0;33m"),
    BLUE("\033[0;34m"),
    MAGENTA("\033[0;35m"),
    CYAN("\033[0;36m"),
    WHITE("\033[0;37m"),

    // Bright Text Colors
    BRIGHT_BLACK("\033[0;90m"),
    BRIGHT_RED("\033[0;91m"),
    BRIGHT_GREEN("\033[0;92m"),
    BRIGHT_YELLOW("\033[0;93m"),
    BRIGHT_BLUE("\033[0;94m"),
    BRIGHT_MAGENTA("\033[0;95m"),
    BRIGHT_CYAN("\033[0;96m"),
    BRIGHT_WHITE("\033[0;97m"),

    // Background Colors
    BG_BLACK("\033[40m"),
    BG_RED("\033[41m"),
    BG_GREEN("\033[42m"),
    BG_YELLOW("\033[43m"),
    BG_BLUE("\033[44m"),
    BG_MAGENTA("\033[45m"),
    BG_CYAN("\033[46m"),
    BG_WHITE("\033[47m"),

    // Bright Background Colors
    BG_BRIGHT_BLACK("\033[100m"),
    BG_BRIGHT_RED("\033[101m"),
    BG_BRIGHT_GREEN("\033[102m"),
    BG_BRIGHT_YELLOW("\033[103m"),
    BG_BRIGHT_BLUE("\033[104m"),
    BG_BRIGHT_MAGENTA("\033[105m"),
    BG_BRIGHT_CYAN("\033[106m"),
    BG_BRIGHT_WHITE("\033[107m"),

    // Text Styles
    BOLD("\033[1m"),
    UNDERLINE("\033[4m"),
    REVERSED("\033[7m");

    private final String code;

    TextColor(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }
}
