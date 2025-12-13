package org.strassburger.tui4j.formatting.ansi;

public enum AnsiCode {
    // General ASCII Codes
    BEL("\u0007"),
    BS("\u0008"),
    HT("\u0009"),
    LF("\n"),
    VT("\u000B"),
    FF("\u000C"),
    CR("\r"),
    ESC("\u001B"),
    DEL("\u007F"),

    // Cursor Controls
    CURSOR_HOME("\u001B[H"),
    CURSOR_POSITION("\u001B[%d;%dH"), // format line;column
    CURSOR_UP("\u001B[%dA"),
    CURSOR_DOWN("\u001B[%dB"),
    CURSOR_FORWARD("\u001B[%dC"),
    CURSOR_BACK("\u001B[%dD"),
    CURSOR_NEXT_LINE("\u001B[%dE"),
    CURSOR_PREVIOUS_LINE("\u001B[%dF"),
    CURSOR_COLUMN("\u001B[%dG"),
    CURSOR_SAVE_DEC("\u001B7"),
    CURSOR_RESTORE_DEC("\u001B8"),
    CURSOR_SAVE_SCO("\u001Bs"),
    CURSOR_RESTORE_SCO("\u001Bu"),
    CURSOR_POSITION_REQUEST("\u001B[6n"),

    // Erase Functions
    ERASE_DISPLAY("\u001B[J"),
    ERASE_DISPLAY_CURSOR_TO_END("\u001B[0J"),
    ERASE_DISPLAY_CURSOR_TO_BEGINNING("\u001B[1J"),
    ERASE_DISPLAY_ENTIRE("\u001B[2J"),
    ERASE_DISPLAY_SAVED_LINES("\u001B[3J"),
    ERASE_LINE("\u001B[K"),
    ERASE_LINE_CURSOR_TO_END("\u001B[0K"),
    ERASE_LINE_START_TO_CURSOR("\u001B[1K"),
    ERASE_LINE_ENTIRE("\u001B[2K"),

    // Styles / Graphics Modes
    RESET("\u001B[0m"),
    BOLD("\u001B[1m"),
    DIM("\u001B[2m"),
    ITALIC("\u001B[3m"),
    UNDERLINE("\u001B[4m"),
    BLINK("\u001B[5m"),
    INVERSE("\u001B[7m"),
    HIDDEN("\u001B[8m"),
    STRIKETHROUGH("\u001B[9m"),
    BOLD_OFF("\u001B[22m"),
    ITALIC_OFF("\u001B[23m"),
    UNDERLINE_OFF("\u001B[24m"),
    BLINK_OFF("\u001B[25m"),
    INVERSE_OFF("\u001B[27m"),
    HIDDEN_OFF("\u001B[28m"),
    STRIKETHROUGH_OFF("\u001B[29m"),

    // Foreground Colors (8 colors)
    FG_BLACK("\u001B[30m"),
    FG_RED("\u001B[31m"),
    FG_GREEN("\u001B[32m"),
    FG_YELLOW("\u001B[33m"),
    FG_BLUE("\u001B[34m"),
    FG_MAGENTA("\u001B[35m"),
    FG_CYAN("\u001B[36m"),
    FG_WHITE("\u001B[37m"),
    FG_DEFAULT("\u001B[39m"),

    // Background Colors (8 colors)
    BG_BLACK("\u001B[40m"),
    BG_RED("\u001B[41m"),
    BG_GREEN("\u001B[42m"),
    BG_YELLOW("\u001B[43m"),
    BG_BLUE("\u001B[44m"),
    BG_MAGENTA("\u001B[45m"),
    BG_CYAN("\u001B[46m"),
    BG_WHITE("\u001B[47m"),
    BG_DEFAULT("\u001B[49m"),

    // Bright Foreground Colors
    FG_BRIGHT_BLACK("\u001B[90m"),
    FG_BRIGHT_RED("\u001B[91m"),
    FG_BRIGHT_GREEN("\u001B[92m"),
    FG_BRIGHT_YELLOW("\u001B[93m"),
    FG_BRIGHT_BLUE("\u001B[94m"),
    FG_BRIGHT_MAGENTA("\u001B[95m"),
    FG_BRIGHT_CYAN("\u001B[96m"),
    FG_BRIGHT_WHITE("\u001B[97m"),

    // Bright Background Colors
    BG_BRIGHT_BLACK("\u001B[100m"),
    BG_BRIGHT_RED("\u001B[101m"),
    BG_BRIGHT_GREEN("\u001B[102m"),
    BG_BRIGHT_YELLOW("\u001B[103m"),
    BG_BRIGHT_BLUE("\u001B[104m"),
    BG_BRIGHT_MAGENTA("\u001B[105m"),
    BG_BRIGHT_CYAN("\u001B[106m"),
    BG_BRIGHT_WHITE("\u001B[107m");

    private final String code;

    AnsiCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }

    /**
     * Formats the ANSI code with the given arguments.
     * @param args the arguments to format the code
     * @return the formatted ANSI code string
     */
    public String format(Object... args) {
        return String.format(code, args);
    }
}
