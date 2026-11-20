package org.strassburger.tui4j.formatting.layout.box;

import org.jspecify.annotations.Nullable;
import org.strassburger.tui4j.console.Printer;
import org.strassburger.tui4j.formatting.StyledText;
import org.strassburger.tui4j.formatting.layout.Renderable;
import org.strassburger.tui4j.formatting.layout.box.styles.BoxAlign;
import org.strassburger.tui4j.formatting.layout.box.styles.BoxStyle;
import org.strassburger.tui4j.formatting.layout.box.styles.BoxStyles;
import org.strassburger.tui4j.formatting.layout.box.styles.BoxTitlePosition;

import java.util.List;

/**
 * A layout component that draws a border around its content, with an optional title in the top or bottom border.
 * Line breaks ({@code \n}) in the content are always respected.
 */
public class Box implements Renderable {
    private StyledText content;
    private @Nullable StyledText title = null;
    private BoxTitlePosition titlePosition = BoxTitlePosition.TOP_LEFT;
    private BoxStyle style;
    private BoxAlign align;
    private boolean grow = false;
    private boolean wrap = true;

    // padding overrides (null = use the style's padding)
    private @Nullable Integer paddingTop = null;
    private @Nullable Integer paddingBottom = null;
    private @Nullable Integer paddingLeft = null;
    private @Nullable Integer paddingRight = null;

    private int marginTop = 0;
    private int marginBottom = 0;
    private int marginLeft = 0;
    private int marginRight = 0;

    public Box() {
        this.content = StyledText.text("");
        this.style = BoxStyles.DEFAULT;
        this.align = BoxAlign.START;
    }

    public Box(StyledText content) {
        this();
        this.content = content;
    }

    public Box(String content) {
        this(StyledText.text(content));
    }

    /**
     * Sets the content of the box
     * @param content the content
     * @return the Box instance for chaining
     */
    public Box content(StyledText content) {
        this.content = content;
        return this;
    }

    /**
     * Sets the content of the box
     * @param content the content
     * @return the Box instance for chaining
     */
    public Box content(String content) {
        return content(StyledText.text(content));
    }

    public StyledText content() {
        return content;
    }

    /**
     * Sets the title shown in the border (see {@link #titlePosition(BoxTitlePosition)})
     * @param title the title, or null for no title
     * @return the Box instance for chaining
     */
    public Box title(@Nullable StyledText title) {
        this.title = title;
        return this;
    }

    /**
     * Sets the title shown in the border (see {@link #titlePosition(BoxTitlePosition)})
     * @param title the title
     * @return the Box instance for chaining
     */
    public Box title(String title) {
        return title(StyledText.text(title));
    }

    public @Nullable StyledText title() {
        return title;
    }

    /**
     * Sets where the title is placed in the border
     * @param titlePosition the title position (default {@link BoxTitlePosition#TOP_LEFT})
     * @return the Box instance for chaining
     */
    public Box titlePosition(BoxTitlePosition titlePosition) {
        this.titlePosition = titlePosition;
        return this;
    }

    public BoxTitlePosition titlePosition() {
        return titlePosition;
    }

    /**
     * Sets the style of the box
     * @param style the {@link BoxStyle} to use (e.g. {@link BoxStyles#DEFAULT})
     * @return the Box instance for chaining
     */
    public Box style(BoxStyle style) {
        this.style = style;
        return this;
    }

    public BoxStyle style() {
        return style;
    }

    /**
     * Sets the horizontal alignment of the content
     * @param align the alignment
     * @return the Box instance for chaining
     */
    public Box align(BoxAlign align) {
        this.align = align;
        return this;
    }

    public BoxAlign align() {
        return align;
    }

    /**
     * Sets whether the box grows to fill the available width
     * @param grow true to fill the available width
     * @return the Box instance for chaining
     */
    public Box grow(boolean grow) {
        this.grow = grow;
        return this;
    }

    public boolean grow() {
        return grow;
    }

    /**
     * Sets whether the content is wrapped when the box is wider than the available width
     * @param wrap true to wrap the content
     * @return the Box instance for chaining
     */
    public Box wrap(boolean wrap) {
        this.wrap = wrap;
        return this;
    }

    public boolean wrap() {
        return wrap;
    }


    /**
     * Sets the padding on all sides, overriding the style's padding
     * @param padding the padding (must be >= 0)
     * @return the Box instance for chaining
     */
    public Box padding(int padding) {
        return padding(padding, padding);
    }

    /**
     * Sets the vertical and horizontal padding, overriding the style's padding
     * @param vertical the padding above and below the content in lines (must be >= 0)
     * @param horizontal the padding left and right of the content in characters (must be >= 0)
     * @return the Box instance for chaining
     */
    public Box padding(int vertical, int horizontal) {
        paddingTop(vertical).paddingBottom(vertical);
        return paddingLeft(horizontal).paddingRight(horizontal);
    }

    public Box paddingTop(int padding) {
        this.paddingTop = requireNonNegative(padding, "padding");
        return this;
    }

    public Box paddingBottom(int padding) {
        this.paddingBottom = requireNonNegative(padding, "padding");
        return this;
    }

    public Box paddingLeft(int padding) {
        this.paddingLeft = requireNonNegative(padding, "padding");
        return this;
    }

    public Box paddingRight(int padding) {
        this.paddingRight = requireNonNegative(padding, "padding");
        return this;
    }

    /** Returns the top padding (the style's padding unless overridden) */
    public int paddingTop() {
        return paddingTop != null ? paddingTop : style.paddingTop();
    }

    /** Returns the bottom padding (the style's padding unless overridden) */
    public int paddingBottom() {
        return paddingBottom != null ? paddingBottom : style.paddingBottom();
    }

    /** Returns the left padding (the style's padding unless overridden) */
    public int paddingLeft() {
        return paddingLeft != null ? paddingLeft : style.paddingLeft();
    }

    /** Returns the right padding (the style's padding unless overridden) */
    public int paddingRight() {
        return paddingRight != null ? paddingRight : style.paddingRight();
    }


    /**
     * Sets the margin on all sides
     * @param margin the margin (must be >= 0)
     * @return the Box instance for chaining
     */
    public Box margin(int margin) {
        return margin(margin, margin);
    }

    /**
     * Sets the vertical and horizontal margin
     * @param vertical the empty lines above and below the box (must be >= 0)
     * @param horizontal the spaces left and right of the box (must be >= 0)
     * @return the Box instance for chaining
     */
    public Box margin(int vertical, int horizontal) {
        marginTop(vertical).marginBottom(vertical);
        return marginLeft(horizontal).marginRight(horizontal);
    }

    public Box marginTop(int margin) {
        this.marginTop = requireNonNegative(margin, "margin");
        return this;
    }

    public Box marginBottom(int margin) {
        this.marginBottom = requireNonNegative(margin, "margin");
        return this;
    }

    public Box marginLeft(int margin) {
        this.marginLeft = requireNonNegative(margin, "margin");
        return this;
    }

    public Box marginRight(int margin) {
        this.marginRight = requireNonNegative(margin, "margin");
        return this;
    }

    public int marginTop() {
        return marginTop;
    }

    public int marginBottom() {
        return marginBottom;
    }

    public int marginLeft() {
        return marginLeft;
    }

    public int marginRight() {
        return marginRight;
    }

    private static int requireNonNegative(int value, String name) {
        if (value < 0) throw new IllegalArgumentException(name + " must be >= 0");
        return value;
    }

    @Override
    public void render(Printer printer, int width, int height) {
        int padding = paddingLeft() + paddingRight();
        // borders and horizontal margins
        int borderWidth = 2 + marginLeft + marginRight;

        // width of the content area (between the paddings)
        int contentWidth = Math.max(longestLine(content), titleLength() + MIN_TITLE_DECORATION - padding);

        if (wrap && contentWidth + padding + borderWidth > width) {
            contentWidth = Math.max(1, width - padding - borderWidth);
        }
        if (grow) {
            contentWidth = Math.max(contentWidth, width - padding - borderWidth);
        }
        contentWidth = Math.max(contentWidth, 0);

        int innerWidth = contentWidth + padding;
        List<StyledText> lines = contentLines(Math.max(1, contentWidth));

        for (int i = 0; i < marginTop; i++) printer.println();
        renderBorder(printer, innerWidth, style.topLeft(), style.horizontalTop(), style.topRight(), titlePosition.isTop());
        for (int i = 0; i < paddingTop(); i++) renderLine(printer, StyledText.text(""), contentWidth);
        for (StyledText line : lines) renderLine(printer, line, contentWidth);
        for (int i = 0; i < paddingBottom(); i++) renderLine(printer, StyledText.text(""), contentWidth);
        renderBorder(printer, innerWidth, style.bottomLeft(), style.horizontalBottom(), style.bottomRight(), !titlePosition.isTop());
        for (int i = 0; i < marginBottom; i++) printer.println();
    }

    /** Characters around the title in the border: a space on each side and at least one horizontal on each end */
    private static final int MIN_TITLE_DECORATION = 4;

    private int titleLength() {
        return title == null || title.length() == 0 ? -MIN_TITLE_DECORATION : title.length();
    }

    private List<StyledText> contentLines(int contentWidth) {
        if (wrap) return content.wrap(contentWidth);
        // a width that never breaks lines, so only existing line breaks split the content
        return content.wrap(Math.max(1, longestLine(content)));
    }

    private static int longestLine(StyledText text) {
        int max = 0;
        for (String line : text.plainText().split("\n", -1)) {
            max = Math.max(max, line.length());
        }
        return max;
    }

    private void renderBorder(
            Printer printer,
            int innerWidth,
            char left,
            char horizontal,
            char right,
            boolean withTitle
    ) {
        printer.print(" ".repeat(marginLeft));
        printer.print(border(left));

        if (withTitle && title != null && title.length() > 0 && innerWidth >= MIN_TITLE_DECORATION + 1) {
            int maxTitle = innerWidth - MIN_TITLE_DECORATION;
            StyledText shown = title.length() > maxTitle ? title.substring(0, maxTitle) : title;

            // horizontals around " title " (always at least one on each side)
            int fill = innerWidth - shown.length() - 2;
            int before = switch (titlePosition) {
                case TOP_LEFT, BOTTOM_LEFT -> 1;
                case TOP_RIGHT, BOTTOM_RIGHT -> fill - 1;
                case TOP_CENTER, BOTTOM_CENTER -> fill / 2;
            };

            printer.print(borderRepeat(horizontal, before));
            printer.print(" ");
            printer.print(shown);
            printer.print(" ");
            printer.print(borderRepeat(horizontal, fill - before));
        } else {
            printer.print(borderRepeat(horizontal, innerWidth));
        }

        printer.print(border(right));
        printer.print(" ".repeat(marginRight));
        printer.println();
    }

    private void renderLine(Printer printer, StyledText line, int contentWidth) {
        int space = Math.max(0, contentWidth - line.length());

        printer.print(" ".repeat(marginLeft));
        printer.print(border(style.verticalLeft()));
        printer.print(" ".repeat(paddingLeft()));

        switch (align) {
            case START -> {
                printer.print(line);
                printer.print(" ".repeat(space));
            }
            case END -> {
                printer.print(" ".repeat(space));
                printer.print(line);
            }
            case CENTER -> {
                int left = space / 2;
                printer.print(" ".repeat(left));
                printer.print(line);
                printer.print(" ".repeat(space - left));
            }
        }

        printer.print(" ".repeat(paddingRight()));
        printer.print(border(style.verticalRight()));
        printer.print(" ".repeat(marginRight));
        printer.println();
    }

    private StyledText border(char ch) {
        return StyledText.text(String.valueOf(ch)).fg(style.borderColor());
    }

    private StyledText borderRepeat(char ch, int count) {
        return StyledText.text(String.valueOf(ch))
                .fg(style.borderColor())
                .repeat(count);
    }
}
