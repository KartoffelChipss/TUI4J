package org.strassburger.tui4j.formatting.layout.box;

import org.junit.jupiter.api.Test;
import org.strassburger.tui4j.formatting.StyledText;
import org.strassburger.tui4j.formatting.ansi.AnsiColor;
import org.strassburger.tui4j.formatting.ansi.AnsiRenderer;
import org.strassburger.tui4j.formatting.layout.box.styles.BoxAlign;
import org.strassburger.tui4j.formatting.layout.box.styles.BoxStyle;
import org.strassburger.tui4j.formatting.layout.box.styles.BoxStyles;
import org.strassburger.tui4j.formatting.layout.box.styles.BoxTitlePosition;
import org.strassburger.tui4j.formatting.layout.box.styles.DefaultBoxStyle;
import org.strassburger.tui4j.printer.TestPrinter;

import static org.junit.jupiter.api.Assertions.*;

class BoxTest {

    private static String render(Box box, int width) {
        TestPrinter printer = new TestPrinter();
        box.render(printer, width, 24);
        return printer.getOutput().replace(System.lineSeparator(), "\n");
    }

    private static String render(Box box) {
        return render(box, 0);
    }

    private static String lines(String... lines) {
        return String.join("\n", lines) + "\n";
    }

    @Test
    void testDefaults() {
        Box box = new Box();
        assertEquals("", box.content().plainText());
        assertNull(box.title());
        assertSame(BoxStyles.DEFAULT, box.style());
        assertEquals(BoxAlign.START, box.align());
        assertFalse(box.grow());
        assertFalse(box.wrap());
        assertEquals(BoxTitlePosition.TOP_LEFT, box.titlePosition());
    }

    @Test
    void testSettersReturnSameInstance() {
        Box box = new Box();
        assertSame(box, box.content("a"));
        assertSame(box, box.content(StyledText.text("a")));
        assertSame(box, box.title("t"));
        assertSame(box, box.title((StyledText) null));
        assertSame(box, box.style(BoxStyles.ROUNDED));
        assertSame(box, box.align(BoxAlign.END));
        assertSame(box, box.grow(true));
        assertSame(box, box.wrap(true));
        assertSame(box, box.titlePosition(BoxTitlePosition.BOTTOM_RIGHT));
    }

    @Test
    void testRenderSimple() {
        assertEquals(lines(
                "+-------+",
                "| hello |",
                "+-------+"
        ), render(new Box("hello")));
    }

    @Test
    void testRenderEmpty() {
        assertEquals(lines(
                "+--+",
                "|  |",
                "+--+"
        ), render(new Box()));
    }

    @Test
    void testRenderMultiline() {
        assertEquals(lines(
                "+-------+",
                "| a     |",
                "| hello |",
                "+-------+"
        ), render(new Box("a\nhello")));
    }

    @Test
    void testRenderAlignment() {
        assertEquals(lines(
                "+-------+",
                "|     a |",
                "| hello |",
                "+-------+"
        ), render(new Box("a\nhello").align(BoxAlign.END)));

        assertEquals(lines(
                "+-------+",
                "|   a   |",
                "| hello |",
                "+-------+"
        ), render(new Box("a\nhello").align(BoxAlign.CENTER)));
    }

    @Test
    void testRenderTitle() {
        assertEquals(lines(
                "+- Hi --------+",
                "| hello world |",
                "+-------------+"
        ), render(new Box("hello world").title("Hi")));
    }

    @Test
    void testTitleWidensBox() {
        assertEquals(lines(
                "+- Title -+",
                "| a       |",
                "+---------+"
        ), render(new Box("a").title("Title")));
    }

    @Test
    void testUnicodeStyles() {
        assertEquals(lines(
                "┌────┐",
                "│ ab │",
                "└────┘"
        ), render(new Box("ab").style(BoxStyles.UNICODE_BOX)));

        assertEquals(lines(
                "╭────╮",
                "│ ab │",
                "╰────╯"
        ), render(new Box("ab").style(BoxStyles.ROUNDED)));

        assertEquals(lines(
                "╔════╗",
                "║ ab ║",
                "╚════╝"
        ), render(new Box("ab").style(BoxStyles.DOUBLE)));

        assertEquals(lines(
                "┏━━━━┓",
                "┃ ab ┃",
                "┗━━━━┛"
        ), render(new Box("ab").style(BoxStyles.HEAVY)));
    }

    @Test
    void testCustomPadding() {
        BoxStyle padded = new DefaultBoxStyle() {
            @Override public int paddingLeft() { return 2; }
            @Override public int paddingRight() { return 0; }
            @Override public int paddingTop() { return 1; }
            @Override public int paddingBottom() { return 1; }
        };
        assertEquals(lines(
                "+----+",
                "|    |",
                "|  ab|",
                "|    |",
                "+----+"
        ), render(new Box("ab").style(padded)));
    }

    @Test
    void testGrowFillsWidth() {
        assertEquals(lines(
                "+--------+",
                "| ab     |",
                "+--------+"
        ), render(new Box("ab").grow(true), 10));
    }

    @Test
    void testNoGrowIgnoresWidth() {
        assertEquals(render(new Box("ab")), render(new Box("ab"), 80));
    }

    @Test
    void testWrap() {
        assertEquals(lines(
                "+-------+",
                "| hello |",
                "| world |",
                "+-------+"
        ), render(new Box("hello world").wrap(true), 9));
    }

    @Test
    void testWrapDoesNothingWhenFits() {
        assertEquals(render(new Box("hello world"), 80), render(new Box("hello world").wrap(true), 80));
    }

    @Test
    void testWrapTruncatesTitle() {
        assertEquals(lines(
                "+- Lon -+",
                "| hello |",
                "| world |",
                "+-------+"
        ), render(new Box("hello world").title("Long title").wrap(true), 9));
    }

    private static String renderTitled(BoxTitlePosition position) {
        return render(new Box("hello world").title("Hi").titlePosition(position));
    }

    @Test
    void testTitlePositionsTop() {
        assertEquals(lines(
                "+---- Hi -----+",
                "| hello world |",
                "+-------------+"
        ), renderTitled(BoxTitlePosition.TOP_CENTER));

        assertEquals(lines(
                "+-------- Hi -+",
                "| hello world |",
                "+-------------+"
        ), renderTitled(BoxTitlePosition.TOP_RIGHT));
    }

    @Test
    void testTitlePositionsBottom() {
        assertEquals(lines(
                "+-------------+",
                "| hello world |",
                "+- Hi --------+"
        ), renderTitled(BoxTitlePosition.BOTTOM_LEFT));

        assertEquals(lines(
                "+-------------+",
                "| hello world |",
                "+---- Hi -----+"
        ), renderTitled(BoxTitlePosition.BOTTOM_CENTER));

        assertEquals(lines(
                "+-------------+",
                "| hello world |",
                "+-------- Hi -+"
        ), renderTitled(BoxTitlePosition.BOTTOM_RIGHT));
    }

    @Test
    void testBottomTitleWidensBox() {
        assertEquals(lines(
                "+---------+",
                "| a       |",
                "+- Title -+"
        ), render(new Box("a").title("Title").titlePosition(BoxTitlePosition.BOTTOM_CENTER)));
    }

    @Test
    void testTitlePositionIsTop() {
        assertTrue(BoxTitlePosition.TOP_LEFT.isTop());
        assertTrue(BoxTitlePosition.TOP_CENTER.isTop());
        assertTrue(BoxTitlePosition.TOP_RIGHT.isTop());
        assertFalse(BoxTitlePosition.BOTTOM_LEFT.isTop());
        assertFalse(BoxTitlePosition.BOTTOM_CENTER.isTop());
        assertFalse(BoxTitlePosition.BOTTOM_RIGHT.isTop());
    }

    // ---------------------------------------------------------------------
    // Padding and margin
    // ---------------------------------------------------------------------

    @Test
    void testPaddingAndMarginDefaults() {
        Box box = new Box();
        assertEquals(0, box.paddingTop());
        assertEquals(0, box.paddingBottom());
        assertEquals(1, box.paddingLeft());
        assertEquals(1, box.paddingRight());
        assertEquals(0, box.marginTop());
        assertEquals(0, box.marginBottom());
        assertEquals(0, box.marginLeft());
        assertEquals(0, box.marginRight());
    }

    @Test
    void testPaddingAndMarginSettersReturnSameInstance() {
        Box box = new Box();
        assertSame(box, box.padding(1));
        assertSame(box, box.padding(1, 2));
        assertSame(box, box.paddingTop(1));
        assertSame(box, box.paddingBottom(1));
        assertSame(box, box.paddingLeft(1));
        assertSame(box, box.paddingRight(1));
        assertSame(box, box.margin(1));
        assertSame(box, box.margin(1, 2));
        assertSame(box, box.marginTop(1));
        assertSame(box, box.marginBottom(1));
        assertSame(box, box.marginLeft(1));
        assertSame(box, box.marginRight(1));
    }

    @Test
    void testNegativePaddingAndMarginRejected() {
        Box box = new Box();
        assertThrows(IllegalArgumentException.class, () -> box.padding(-1));
        assertThrows(IllegalArgumentException.class, () -> box.paddingLeft(-1));
        assertThrows(IllegalArgumentException.class, () -> box.margin(0, -1));
        assertThrows(IllegalArgumentException.class, () -> box.marginTop(-1));
    }

    @Test
    void testPaddingVerticalHorizontal() {
        Box box = new Box("ab").padding(1, 3);
        assertEquals(1, box.paddingTop());
        assertEquals(1, box.paddingBottom());
        assertEquals(3, box.paddingLeft());
        assertEquals(3, box.paddingRight());

        assertEquals(lines(
                "+--------+",
                "|        |",
                "|   ab   |",
                "|        |",
                "+--------+"
        ), render(box));
    }

    @Test
    void testPaddingOverridesStyleOnlyOnSetSides() {
        BoxStyle padded = new DefaultBoxStyle() {
            @Override public int paddingTop() { return 1; }
        };
        Box box = new Box("ab").style(padded).paddingLeft(0);

        assertEquals(1, box.paddingTop());
        assertEquals(0, box.paddingLeft());
        assertEquals(1, box.paddingRight());
        assertEquals(lines(
                "+---+",
                "|   |",
                "|ab |",
                "+---+"
        ), render(box));
    }

    @Test
    void testZeroPadding() {
        assertEquals(lines(
                "+--+",
                "|ab|",
                "+--+"
        ), render(new Box("ab").padding(0)));
    }

    @Test
    void testMarginVerticalHorizontal() {
        assertEquals(lines(
                "",
                "  +----+  ",
                "  | ab |  ",
                "  +----+  ",
                ""
        ), render(new Box("ab").margin(1, 2)));
    }

    @Test
    void testIndividualMargins() {
        assertEquals(lines(
                "",
                "",
                " +----+",
                " | ab |",
                " +----+"
        ), render(new Box("ab").marginTop(2).marginLeft(1)));
    }

    @Test
    void testGrowRespectsMargin() {
        assertEquals(lines(
                "  +----+  ",
                "  | ab |  ",
                "  +----+  "
        ), render(new Box("ab").margin(0, 2).grow(true), 10));
    }

    @Test
    void testWrapRespectsMarginAndPadding() {
        assertEquals(lines(
                " +-------+ ",
                " | hello | ",
                " | world | ",
                " +-------+ "
        ), render(new Box("hello world").margin(0, 1).wrap(true), 11));

        assertEquals(lines(
                "+---------+",
                "|  hello  |",
                "|  world  |",
                "+---------+"
        ), render(new Box("hello world").padding(0, 2).wrap(true), 11));
    }

    @Test
    void testBorderColor() {
        BoxStyle red = BoxStyles.DEFAULT.withBorderColor(AnsiColor.RED);
        assertNotSame(BoxStyles.DEFAULT, red);
        assertEquals(AnsiColor.RED, red.borderColor());
        assertEquals(AnsiColor.DEFAULT, BoxStyles.DEFAULT.borderColor());

        AnsiRenderer ansi = new AnsiRenderer();
        TestPrinter printer = new TestPrinter(ansi);
        new Box("a").style(red).render(printer, 0, 24);
        String output = printer.getOutput();

        assertTrue(output.startsWith(ansi.render(StyledText.text("+").fg(AnsiColor.RED))));
        assertTrue(output.contains(ansi.render(StyledText.text("|").fg(AnsiColor.RED))));
    }
}
