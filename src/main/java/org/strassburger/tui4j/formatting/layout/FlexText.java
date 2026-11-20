package org.strassburger.tui4j.formatting.layout;

import org.strassburger.tui4j.formatting.StyledText;
import org.strassburger.tui4j.formatting.ansi.AnsiColor;
import org.strassburger.tui4j.console.Printer;

import java.util.ArrayList;
import java.util.List;

/**
 * A layout component that arranges multiple StyledText parts with flexible justification and spacing.
 */
public class FlexText implements Renderable {
    private List<StyledText> parts;
    private FlexJustify justify;
    private int gap;
    private StyledText separatorChar = StyledText.text(" ");
    private boolean wrap = false;

    public FlexText() {
        this.parts = new ArrayList<>();
        this.justify = FlexJustify.CENTER;
        this.gap = 3;
    }

    /**
     * Creates a FlexText with a single part
     * @param part the StyledText part to add
     */
    public FlexText(StyledText... part) {
        this();
        this.parts.addAll(List.of(part));
    }

    public List<StyledText> parts() {
        return parts;
    }

    public FlexText parts(List<StyledText> parts) {
        this.parts = new ArrayList<>(parts);
        return this;
    }

    public FlexText part(StyledText part) {
        this.parts.add(part);
        return this;
    }

    public FlexText part(String text) {
        this.parts.add(StyledText.text(text));
        return this;
    }

    public FlexJustify justify() {
        return justify;
    }

    /**
     * Sets the justification mode for arranging parts
     * @param justify the FlexJustify mode
     * @return the FlexText instance
     */
    public FlexText justify(FlexJustify justify) {
        this.justify = justify;
        return this;
    }

    public int gap() {
        return gap;
    }

    /**
     * Sets the fixed gap size between parts
     * @param gap the number of separator characters to insert between parts
     * @return the FlexText instance
     */
    public FlexText gap(int gap) {
        this.gap = gap;
        return this;
    }

    public StyledText separatorChar() {
        return separatorChar;
    }

    /**
     * Sets the character(s) used for spacing between parts
     * @param separatorChar the StyledText to use as the separator
     * @return the FlexText instance
     */
    public FlexText separatorChar(StyledText separatorChar) {
        this.separatorChar = separatorChar;
        return this;
    }

    public boolean wrap() {
        return wrap;
    }

    /**
     * <p>
     * Sets whether the parts wrap when they do not fit into the available width.
     * </p>
     * <p>
     * When enabled, the parts stay next to each other: the widest parts are shrunk until everything
     * fits (keeping at least the gap between parts), and the text of each shrunk part is word-wrapped
     * within its own column. Every resulting line is justified the same way, so the columns stay aligned.
     * </p>
     * @param wrap true to wrap
     * @return the FlexText instance
     */
    public FlexText wrap(boolean wrap) {
        this.wrap = wrap;
        return this;
    }

    @Override
    public void render(Printer printer, int width, int height) {
        if (!wrap || parts.isEmpty() || contentLength(parts) <= width) {
            renderLine(printer, parts, width);
            return;
        }

        int[] colWidths = shrinkToFit(width);

        // wrap every part into its column and narrow the column to its longest line
        List<List<StyledText>> columns = new ArrayList<>(parts.size());
        int rowCount = 1;
        for (int i = 0; i < parts.size(); i++) {
            List<StyledText> lines = parts.get(i).wrap(colWidths[i]);
            int longest = 0;
            for (StyledText line : lines) longest = Math.max(longest, line.length());
            colWidths[i] = longest;
            columns.add(lines);
            rowCount = Math.max(rowCount, lines.size());
        }

        for (int row = 0; row < rowCount; row++) {
            if (row > 0) printer.println();

            List<StyledText> rowParts = new ArrayList<>(parts.size());
            for (int i = 0; i < parts.size(); i++) {
                List<StyledText> lines = columns.get(i);
                StyledText line = row < lines.size() ? lines.get(row) : StyledText.text("");
                rowParts.add(line.append(" ".repeat(colWidths[i] - line.length())));
            }
            renderLine(printer, rowParts, width);
        }
    }

    /**
     * Computes the width of every part, shrinking the widest parts until all parts
     * plus the gaps between them fit into the given width (a part never gets narrower than one character)
     */
    private int[] shrinkToFit(int width) {
        int[] colWidths = new int[parts.size()];
        int total = gap * (parts.size() - 1);
        for (int i = 0; i < parts.size(); i++) {
            colWidths[i] = Math.max(1, parts.get(i).length());
            total += colWidths[i];
        }

        while (total > width) {
            int widest = -1;
            for (int i = 0; i < colWidths.length; i++) {
                if (colWidths[i] > 1 && (widest == -1 || colWidths[i] > colWidths[widest])) widest = i;
            }
            if (widest == -1) break; // cannot shrink any further

            colWidths[widest]--;
            total--;
        }

        return colWidths;
    }

    /**
     * Renders a single line of parts according to the justification mode
     */
    private void renderLine(Printer printer, List<StyledText> parts, int width) {
        StyledText gapStr = separatorChar.repeat(gap);

        switch (justify) {
            case START: {
                int free = Math.max(0, width - contentLength(parts));
                printPartsWithGap(printer, parts, gapStr);
                printer.print(separatorChar.repeat(free));
                break;
            }
            case CENTER: {
                int free = Math.max(0, width - contentLength(parts));
                int left = free / 2;
                printer.print(separatorChar.repeat(left));
                printPartsWithGap(printer, parts, gapStr);
                printer.print(separatorChar.repeat(free - left));
                break;
            }
            case END: {
                int free = Math.max(0, width - contentLength(parts));
                printer.print(separatorChar.repeat(free));
                printPartsWithGap(printer, parts, gapStr);
                break;
            }
            case SPACE_BETWEEN:
                int totalPartsLen = parts.stream()
                        .mapToInt(StyledText::length)
                        .sum();
                int spaceAvailable = width - totalPartsLen;
                int gapsCount = parts.size() - 1;
                int spacePerGap = gapsCount > 0 ? spaceAvailable / gapsCount : 0;
                StyledText dynamicGap = separatorChar.repeat(spacePerGap);

                for (int i = 0; i < parts.size(); i++) {
                    printer.print(parts.get(i));
                    if (i < parts.size() - 1) {
                        printer.print(dynamicGap);
                    }
                }
                break;
            case SPACE_AROUND:
                int partsLenAround = parts.stream()
                        .mapToInt(StyledText::length)
                        .sum();

                int spaceAvailAround = width - partsLenAround;
                int unitCount = parts.size() * 2;
                int unit = unitCount > 0 ? spaceAvailAround / unitCount : 0;

                StyledText edgeGap = separatorChar.repeat(unit);
                StyledText innerGap = separatorChar.repeat(unit * 2);

                printer.print(edgeGap);
                for (int i = 0; i < parts.size(); i++) {
                    printer.print(parts.get(i));
                    if (i < parts.size() - 1) {
                        printer.print(innerGap);
                    }
                }
                printer.print(edgeGap);
                break;
            case SPACE_EVENLY:
                int total_Length = parts.stream()
                        .mapToInt(StyledText::length)
                        .sum();
                int space_Avail = width - total_Length;
                int gaps_Cnt = parts.size() + 1;
                int space_Per_Gap = gaps_Cnt > 0 ? space_Avail / gaps_Cnt : 0;
                StyledText dynamic_Gap = separatorChar.repeat(space_Per_Gap);

                printer.print(dynamic_Gap);
                for (StyledText part : parts) {
                    printer.print(part);
                    printer.print(dynamic_Gap);
                }
                break;
        }
    }

    /**
     * Returns the plain-text length of all parts plus the fixed gaps between them
     */
    private int contentLength(List<StyledText> parts) {
        int partsLength = parts.stream()
                .mapToInt(StyledText::length)
                .sum();
        int gapCount = Math.max(0, parts.size() - 1);
        return partsLength + gap * gapCount;
    }

    /**
     * Prints all parts with the given gap between them (not after the last one)
     */
    private void printPartsWithGap(Printer printer, List<StyledText> parts, StyledText gapStr) {
        for (int i = 0; i < parts.size(); i++) {
            if (i > 0) printer.print(gapStr);
            printer.print(parts.get(i));
        }
    }

    /**
     * Creates a FlexText representing a key-value pair with a clear line separation
     * @param key the key string
     * @param value the value string
     * @return the constructed FlexText
     */
    public static FlexText keyValue(String key, String value) {
        return keyValue(StyledText.text(key).fg(AnsiColor.BRIGHT_WHITE), StyledText.text(value).fg(AnsiColor.BRIGHT_WHITE));
    }

    /**
     * Creates a FlexText representing a key-value pair with a clear line separation
     * @param key the key StyledText
     * @param value the value StyledText
     * @return the constructed FlexText
     */
    public static FlexText keyValue(StyledText key, StyledText value) {
        return new FlexText()
                .part(key)
                .part(value)
                .justify(FlexJustify.SPACE_BETWEEN)
                .separatorChar(StyledText.text("⋅").fg(AnsiColor.BRIGHT_BLACK));
    }
}
