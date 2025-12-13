package org.strassburger.tui4j.formatting.layout;

import org.strassburger.tui4j.formatting.PlainTextRenderer;
import org.strassburger.tui4j.formatting.StyledText;
import org.strassburger.tui4j.printer.Printer;

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
    private final PlainTextRenderer plainTextRenderer = new PlainTextRenderer();

    public FlexText() {
        this.parts = new ArrayList<>();
        this.justify = FlexJustify.CENTER;
        this.gap = 3;
    }

    public List<StyledText> getParts() {
        return parts;
    }

    public FlexText setParts(List<StyledText> parts) {
        this.parts = parts;
        return this;
    }

    public FlexText addPart(StyledText part) {
        this.parts.add(part);
        return this;
    }

    public FlexText addPart(String text) {
        this.parts.add(StyledText.text(text));
        return this;
    }

    public FlexJustify getJustify() {
        return justify;
    }

    /**
     * Sets the justification mode for arranging parts
     * @param justify the FlexJustify mode
     * @return the FlexText instance
     */
    public FlexText setJustify(FlexJustify justify) {
        this.justify = justify;
        return this;
    }

    public int getGap() {
        return gap;
    }

    /**
     * Sets the fixed gap size between parts
     * @param gap the number of separator characters to insert between parts
     * @return the FlexText instance
     */
    public FlexText setGap(int gap) {
        this.gap = gap;
        return this;
    }

    public StyledText getSeparatorChar() {
        return separatorChar;
    }

    /**
     * Sets the character(s) used for spacing between parts
     * @param separatorChar the StyledText to use as the separator
     * @return the FlexText instance
     */
    public FlexText setSeparatorChar(StyledText separatorChar) {
        this.separatorChar = separatorChar;
        return this;
    }

    @Override
    public void render(Printer printer, int width, int height) {
        StyledText gapStr = separatorChar.repeat(gap);

        switch (justify) {
            case START:
                for (StyledText part : parts) {
                    printer.print(part);
                    printer.print(gapStr);
                }

                int partsLen = parts.stream()
                        .mapToInt(part -> plainTextRenderer.render(part).length())
                        .sum();
                int gapsLen = gap * (parts.size() - 1);
                int len = partsLen + gapsLen;
                int pad = width - len;
                printer.print(separatorChar.repeat(Math.max(0, pad)));
                break;
            case CENTER:
                int totalPartsLength = parts.stream()
                        .mapToInt(part -> plainTextRenderer.render(part).length())
                        .sum();
                int totalGapsLength = gap * (parts.size() - 1);
                int totalLength = totalPartsLength + totalGapsLength;

                int leftPadding = (width - totalLength) / 2;
                printer.print(separatorChar.repeat(Math.max(0, leftPadding)));

                for (StyledText part : parts) {
                    printer.print(part);
                    printer.print(gapStr);
                }

                int rightPadding = width - leftPadding - totalLength;
                printer.print(separatorChar.repeat(Math.max(0, rightPadding)));
                break;
            case END:
                int partsLength = parts.stream()
                        .mapToInt(part -> plainTextRenderer.render(part).length())
                        .sum();
                int gapsLength = gap * (parts.size() - 1);
                int length = partsLength + gapsLength;
                int padding = width - length;
                printer.print(separatorChar.repeat(Math.max(0, padding)));
                for (StyledText part : parts) {
                    printer.print(part);
                    printer.print(gapStr);
                }
                break;
            case SPACE_BETWEEN:
                int totalPartsLen = parts.stream()
                        .mapToInt(part -> plainTextRenderer.render(part).length())
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
                        .mapToInt(part -> plainTextRenderer.render(part).length())
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
                        .mapToInt(part -> plainTextRenderer.render(part).length())
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
}
