package org.strassburger.tui4j.formatting.ansi;

import org.strassburger.colorlab4j.color.Color;
import org.strassburger.colorlab4j.color.spaces.*;

/**
 * Represents an ANSI color that can be used in terminal styling
 */
public final class AnsiColor extends Color {
    private final int fgCode;
    private final int bgCode;

    public static final AnsiColor BLACK = new AnsiColor(30, 40);
    public static final AnsiColor RED = new AnsiColor(31, 41);
    public static final AnsiColor GREEN = new AnsiColor(32, 42);
    public static final AnsiColor YELLOW = new AnsiColor(33, 43);
    public static final AnsiColor BLUE = new AnsiColor(34, 44);
    public static final AnsiColor MAGENTA = new AnsiColor(35, 45);
    public static final AnsiColor CYAN = new AnsiColor(36, 46);
    public static final AnsiColor WHITE = new AnsiColor(37, 47);

    public static final AnsiColor BRIGHT_BLACK = new AnsiColor(90, 100);
    public static final AnsiColor BRIGHT_RED = new AnsiColor(91, 101);
    public static final AnsiColor BRIGHT_GREEN = new AnsiColor(92, 102);
    public static final AnsiColor BRIGHT_YELLOW = new AnsiColor(93, 103);
    public static final AnsiColor BRIGHT_BLUE = new AnsiColor(94, 104);
    public static final AnsiColor BRIGHT_MAGENTA = new AnsiColor(95, 105);
    public static final AnsiColor BRIGHT_CYAN = new AnsiColor(96, 106);
    public static final AnsiColor BRIGHT_WHITE = new AnsiColor(97, 107);

    private AnsiColor(int fgCode, int bgCode) {
        this.fgCode = fgCode;
        this.bgCode = bgCode;
    }

    /**
     * Converts this ANSI color to its RGB representation. <br/>
     * <b>Note: ANSI colors do not have direct RGB values, so this is an approximation.</b>
     * @return RGBColor representation of this ANSI color.
     */
    @Override
    public RGBColor toRGB() {
        // ANSI colors do not have direct RGB values, so we map them to standard RGB approximations
        return switch (fgCode) {
            case 30 -> new RGBColor(0, 0, 0);
            case 31, 91 -> new RGBColor(255, 0, 0);
            case 32, 92 -> new RGBColor(0, 255, 0);
            case 33, 93 -> new RGBColor(255, 255, 0);
            case 34, 94 -> new RGBColor(0, 0, 255);
            case 35, 95 -> new RGBColor(255, 0, 255);
            case 36, 96 -> new RGBColor(0, 255, 255);
            case 37, 97 -> new RGBColor(255, 255, 255);
            case 90 -> new RGBColor(128, 128, 128);
            default -> new RGBColor(0, 0, 0); // Default to black
        };
    }

    @Override
    public HSLColor toHSL() {
        return toRGB().toHSL();
    }

    @Override
    public HSVColor toHSV() {
        return toRGB().toHSV();
    }

    @Override
    public LABColor toLAB() {
        return toRGB().toLAB();
    }

    @Override
    public XYZColor toXYZ() {
        return toRGB().toXYZ();
    }

    @Override
    public String toCssString() {
        return toRGB().toCssString();
    }

    @Override
    public String toAnsi() {
        return "\u001B[" + fgCode + "m";
    }

    @Override
    public String toAnsiBackground() {
        return "\u001B[" + bgCode + "m";
    }
}

