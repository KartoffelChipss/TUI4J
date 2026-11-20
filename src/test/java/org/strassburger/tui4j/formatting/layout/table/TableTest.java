package org.strassburger.tui4j.formatting.layout.table;

import org.junit.jupiter.api.Test;
import org.strassburger.tui4j.formatting.PlainTextRenderer;
import org.strassburger.tui4j.formatting.Style;
import org.strassburger.tui4j.formatting.StyledText;
import org.strassburger.tui4j.formatting.ansi.AnsiColor;
import org.strassburger.tui4j.formatting.ansi.AnsiRenderer;
import org.strassburger.tui4j.formatting.layout.table.styles.ColumnAlign;
import org.strassburger.tui4j.formatting.layout.table.styles.DefaultTableStyle;
import org.strassburger.tui4j.formatting.layout.table.styles.TableStyle;
import org.strassburger.tui4j.formatting.layout.table.styles.TableStyles;
import org.strassburger.tui4j.printer.TestPrinter;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TableTest {
    private final PlainTextRenderer plain = new PlainTextRenderer();

    private List<String> plainRow(Table table, int index) {
        return table.rows().get(index).stream().map(plain::render).toList();
    }

    /** Renders the table at the given width and returns the output with normalized line endings. */
    private static String render(Table table, int width) {
        TestPrinter printer = new TestPrinter();
        table.render(printer, width, 24);
        return printer.getOutput().replace(System.lineSeparator(), "\n");
    }

    /** Renders at a width small enough that grow never applies. */
    private static String render(Table table) {
        return render(table, 0);
    }

    private static String lines(String... lines) {
        return String.join("\n", lines) + "\n";
    }

    private static Table nameAgeTable() {
        return new Table()
                .row("Name", "Age")
                .row("Alice", "30");
    }

    // ---------------------------------------------------------------------
    // Defaults and accessors
    // ---------------------------------------------------------------------

    @Test
    void testDefaults() {
        Table table = new Table();

        assertTrue(table.rows().isEmpty());
        assertFalse(table.hasHeader());
        assertSame(TableStyles.DEFAULT, table.style());
        assertNull(table.defaultGrow());
        assertEquals(ColumnAlign.START, table.columnAlignment(0));
        assertEquals(0, table.columnGrow(0));
    }

    @Test
    void testSettersReturnSameInstance() {
        Table table = new Table();

        assertSame(table, table.rows(new ArrayList<>()));
        assertSame(table, table.row(List.of(StyledText.text("a"))));
        assertSame(table, table.row("a"));
        assertSame(table, table.stringRow(List.of("a")));
        assertSame(table, table.hasHeader(true));
        assertSame(table, table.columnAlignment(0, ColumnAlign.END));
        assertSame(table, table.columnGrow(0, 1));
        assertSame(table, table.defaultGrow(1));
        assertSame(table, table.style(TableStyles.LIGHT));
    }

    @Test
    void testHasHeader() {
        Table table = new Table().hasHeader(true);
        assertTrue(table.hasHeader());

        table.hasHeader(false);
        assertFalse(table.hasHeader());
    }

    @Test
    void testStyle() {
        Table table = new Table().style(TableStyles.UNICODE_BOX);
        assertSame(TableStyles.UNICODE_BOX, table.style());
    }

    @Test
    void testColumnAlignment() {
        Table table = new Table()
                .columnAlignment(1, ColumnAlign.CENTER)
                .columnAlignment(2, ColumnAlign.END);

        assertEquals(ColumnAlign.START, table.columnAlignment(0));
        assertEquals(ColumnAlign.CENTER, table.columnAlignment(1));
        assertEquals(ColumnAlign.END, table.columnAlignment(2));
    }

    @Test
    void testColumnAlignmentOverwrite() {
        Table table = new Table()
                .columnAlignment(0, ColumnAlign.END)
                .columnAlignment(0, ColumnAlign.CENTER);

        assertEquals(ColumnAlign.CENTER, table.columnAlignment(0));
    }

    @Test
    void testColumnGrow() {
        Table table = new Table().columnGrow(1, 3);

        assertEquals(0, table.columnGrow(0));
        assertEquals(3, table.columnGrow(1));
    }

    @Test
    void testColumnGrowZeroIsAllowed() {
        assertDoesNotThrow(() -> new Table().columnGrow(0, 0));
    }

    @Test
    void testColumnGrowNegativeThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Table().columnGrow(0, -1));
    }

    @Test
    void testDefaultGrow() {
        Table table = new Table().defaultGrow(2);

        assertEquals(2, table.defaultGrow());
        assertEquals(2, table.columnGrow(0));
        assertEquals(2, table.columnGrow(5));
    }

    @Test
    void testColumnGrowOverridesDefaultGrow() {
        Table table = new Table()
                .defaultGrow(2)
                .columnGrow(1, 0);

        assertEquals(2, table.columnGrow(0));
        assertEquals(0, table.columnGrow(1));
    }

    @Test
    void testDefaultGrowZeroIsAllowed() {
        assertEquals(0, new Table().defaultGrow(0).defaultGrow());
    }

    @Test
    void testDefaultGrowNegativeThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Table().defaultGrow(-1));
    }

    // ---------------------------------------------------------------------
    // Adding rows
    // ---------------------------------------------------------------------

    @Test
    void testRowWithStyledText() {
        Table table = new Table().row(List.of(StyledText.text("Name"), StyledText.text("Age")));

        assertEquals(1, table.rows().size());
        assertEquals(List.of("Name", "Age"), plainRow(table, 0));
    }

    @Test
    void testRowVarargsAddsStringCells() {
        Table table = new Table().row("Name", "Age");

        assertEquals(1, table.rows().size());
        assertEquals(List.of("Name", "Age"), plainRow(table, 0));
    }

    @Test
    void testRowVarargsChaining() {
        Table table = new Table()
                .row("Name", "Age")
                .row("Alice", "30")
                .row("Bob", "25");

        assertEquals(3, table.rows().size());
        assertEquals(List.of("Alice", "30"), plainRow(table, 1));
        assertEquals(List.of("Bob", "25"), plainRow(table, 2));
    }

    @Test
    void testRowVarargsWithArray() {
        String[] cells = {"a", "b", "c"};
        Table table = new Table().row(cells);

        assertEquals(List.of("a", "b", "c"), plainRow(table, 0));
    }

    @Test
    void testRowVarargsEmpty() {
        Table table = new Table().row();

        assertEquals(1, table.rows().size());
        assertTrue(table.rows().get(0).isEmpty());
    }

    @Test
    void testStringRowAddsStringCells() {
        Table table = new Table().stringRow(List.of("Name", "Age"));

        assertEquals(1, table.rows().size());
        assertEquals(List.of("Name", "Age"), plainRow(table, 0));
    }

    @Test
    void testStringRowEmpty() {
        Table table = new Table().stringRow(List.of());

        assertEquals(1, table.rows().size());
        assertTrue(table.rows().get(0).isEmpty());
    }

    @Test
    void testStringRowChainingAndMixingWithOtherRowMethods() {
        Table table = new Table()
                .stringRow(List.of("Name", "Age"))
                .row("Alice", "30")
                .row(List.of(StyledText.text("Bob"), StyledText.text("25")));

        assertEquals(3, table.rows().size());
        assertEquals(List.of("Name", "Age"), plainRow(table, 0));
        assertEquals(List.of("Alice", "30"), plainRow(table, 1));
        assertEquals(List.of("Bob", "25"), plainRow(table, 2));
    }

    @Test
    void testRowsReplacesExistingRows() {
        Table table = new Table()
                .row("old")
                .rows(List.of(List.of(StyledText.text("new"))));

        assertEquals(1, table.rows().size());
        assertEquals(List.of("new"), plainRow(table, 0));
    }

    @Test
    void testRowsCopiesTheGivenList() {
        List<List<StyledText>> source = new ArrayList<>();
        source.add(List.of(StyledText.text("a")));

        Table table = new Table().rows(source);
        table.row("b");

        assertEquals(1, source.size());
        assertEquals(2, table.rows().size());
    }

    @Test
    void testRowsCopiesImmutableListSoRowCanBeAddedAfterwards() {
        Table table = new Table()
                .rows(List.of(List.of(StyledText.text("Name"))));

        assertDoesNotThrow(() -> table.row("Alice"));
        assertDoesNotThrow(() -> table.stringRow(List.of("Bob")));
        assertDoesNotThrow(() -> table.row(List.of(StyledText.text("Carol"))));
        assertEquals(4, table.rows().size());
    }

    // ---------------------------------------------------------------------
    // Rendering: structure
    // ---------------------------------------------------------------------

    @Test
    void testRenderEmptyTablePrintsNothing() {
        assertEquals("", render(new Table()));
    }

    @Test
    void testRenderWithoutHeader() {
        assertEquals(lines(
                "+-------+-----+",
                "| Name  | Age |",
                "| Alice | 30  |",
                "+-------+-----+"
        ), render(nameAgeTable()));
    }

    @Test
    void testRenderWithHeader() {
        assertEquals(lines(
                "+-------+-----+",
                "| Name  | Age |",
                "+-------+-----+",
                "| Alice | 30  |",
                "+-------+-----+"
        ), render(nameAgeTable().hasHeader(true)));
    }

    @Test
    void testRenderHeaderOnly() {
        assertEquals(lines(
                "+------+-----+",
                "| Name | Age |",
                "+------+-----+",
                "+------+-----+"
        ), render(new Table().hasHeader(true).row("Name", "Age")));
    }

    @Test
    void testRenderHeaderSeparatorOnlyAfterFirstRow() {
        assertEquals(lines(
                "+---+",
                "| a |",
                "+---+",
                "| b |",
                "| c |",
                "+---+"
        ), render(new Table().hasHeader(true).row("a").row("b").row("c")));
    }

    @Test
    void testRenderSingleCell() {
        assertEquals(lines(
                "+----+",
                "| hi |",
                "+----+"
        ), render(new Table().row("hi")));
    }

    @Test
    void testRenderEmptyStringCell() {
        assertEquals(lines(
                "+--+",
                "|  |",
                "+--+"
        ), render(new Table().row("")));
    }

    @Test
    void testRenderRaggedRowsFillsMissingCells() {
        assertEquals(lines(
                "+---+---+---+",
                "| a | b | c |",
                "| d |   |   |",
                "+---+---+---+"
        ), render(new Table().row("a", "b", "c").row("d")));
    }

    @Test
    void testRenderColumnWidthIsWidestCell() {
        assertEquals(lines(
                "+--------+",
                "| a      |",
                "| abcdef |",
                "| abc    |",
                "+--------+"
        ), render(new Table().row("a").row("abcdef").row("abc")));
    }

    @Test
    void testRenderUsesPlainTextLengthForStyledCells() {
        StyledText styled = StyledText.text("ab", new Style().bold()).append("cd");

        assertEquals(lines(
                "+------+",
                "| abcd |",
                "| x    |",
                "+------+"
        ), render(new Table().row(List.of(styled)).row("x")));
    }

    @Test
    void testRenderAllLinesHaveSameWidth() {
        Table table = new Table()
                .hasHeader(true)
                .row("Name", "Age", "City")
                .row("Alice", "30")
                .row("Bob", "25", "Berlin");

        String[] output = render(table).split("\n");
        for (String line : output) {
            assertEquals(output[0].length(), line.length());
        }
    }

    @Test
    void testRenderCanBeRepeated() {
        Table table = nameAgeTable().hasHeader(true);
        assertEquals(render(table), render(table));
    }

    @Test
    void testRenderViaPrinterUsesPrinterWidth() {
        Table table = new Table().defaultGrow(1).row("a");

        TestPrinter printer = new TestPrinter();
        printer.setWidth(9);
        printer.print(table);

        assertEquals(lines(
                "+-------+",
                "| a     |",
                "+-------+"
        ), printer.getOutput().replace(System.lineSeparator(), "\n"));
    }

    // ---------------------------------------------------------------------
    // Rendering: alignment
    // ---------------------------------------------------------------------

    @Test
    void testRenderAlignStart() {
        Table table = new Table()
                .columnAlignment(0, ColumnAlign.START)
                .row("abcde").row("ab");

        assertEquals(lines(
                "+-------+",
                "| abcde |",
                "| ab    |",
                "+-------+"
        ), render(table));
    }

    @Test
    void testRenderAlignEnd() {
        Table table = new Table()
                .columnAlignment(0, ColumnAlign.END)
                .row("abcde").row("ab");

        assertEquals(lines(
                "+-------+",
                "| abcde |",
                "|    ab |",
                "+-------+"
        ), render(table));
    }

    @Test
    void testRenderAlignCenterPutsExtraSpaceOnTheRight() {
        Table table = new Table()
                .columnAlignment(0, ColumnAlign.CENTER)
                .row("abcde").row("ab").row("a");

        assertEquals(lines(
                "+-------+",
                "| abcde |",
                "|  ab   |",
                "|   a   |",
                "+-------+"
        ), render(table));
    }

    @Test
    void testRenderAlignmentIsPerColumn() {
        Table table = new Table()
                .columnAlignment(1, ColumnAlign.END)
                .columnAlignment(2, ColumnAlign.CENTER)
                .row("aaa", "bbb", "ccc")
                .row("a", "b", "c");

        assertEquals(lines(
                "+-----+-----+-----+",
                "| aaa | bbb | ccc |",
                "| a   |   b |  c  |",
                "+-----+-----+-----+"
        ), render(table));
    }

    @Test
    void testRenderAlignmentAppliesToMissingCells() {
        Table table = new Table()
                .columnAlignment(1, ColumnAlign.END)
                .row("a", "bbb")
                .row("c");

        assertEquals(lines(
                "+---+-----+",
                "| a | bbb |",
                "| c |     |",
                "+---+-----+"
        ), render(table));
    }

    // ---------------------------------------------------------------------
    // Rendering: grow
    // ---------------------------------------------------------------------

    @Test
    void testRenderWithoutGrowIgnoresWidth() {
        assertEquals(render(nameAgeTable(), 0), render(nameAgeTable(), 200));
    }

    @Test
    void testRenderSingleColumnGrowFillsWidth() {
        Table table = new Table().columnGrow(0, 1).row("ab");

        assertEquals(lines(
                "+--------+",
                "| ab     |",
                "+--------+"
        ), render(table, 10));
    }

    @Test
    void testRenderGrowOnlyAffectsGrowingColumns() {
        Table table = new Table().columnGrow(1, 1).row("a", "b");

        assertEquals(lines(
                "+---+------+",
                "| a | b    |",
                "+---+------+"
        ), render(table, 12));
    }

    @Test
    void testRenderGrowDistributesProportionallyWithLeftoverToFirstColumn() {
        // available = 20 - 6 (content) - 3 (borders) = 11
        // col 0: 11 * 1 / 3 = 3, col 1: 11 * 2 / 3 = 7, leftover 1 goes to col 0
        Table table = new Table()
                .columnGrow(0, 1)
                .columnGrow(1, 2)
                .row("a", "b");

        String output = render(table, 20);
        assertEquals(lines(
                "+-------+----------+",
                "| a     | b        |",
                "+-------+----------+"
        ), output);
        assertEquals(20, output.split("\n")[0].length());
    }

    @Test
    void testRenderDefaultGrowAppliesToAllColumns() {
        // available = 11, each column gets 5, leftover 1 goes to col 0
        Table table = new Table().defaultGrow(1).row("a", "b");

        assertEquals(lines(
                "+---------+--------+",
                "| a       | b      |",
                "+---------+--------+"
        ), render(table, 20));
    }

    @Test
    void testRenderColumnGrowOverridesDefaultGrow() {
        Table table = new Table()
                .defaultGrow(1)
                .columnGrow(1, 0)
                .row("a", "b");

        assertEquals(lines(
                "+--------------+---+",
                "| a            | b |",
                "+--------------+---+"
        ), render(table, 20));
    }

    @Test
    void testRenderGrowLeftoverSkipsNonGrowingColumns() {
        // available = 20 - 9 (content) - 4 (borders) = 7
        // col 1: 7 * 1 / 3 = 2, col 2: 7 * 2 / 3 = 4, leftover 1 skips col 0 and goes to col 1
        Table table = new Table()
                .columnGrow(1, 1)
                .columnGrow(2, 2)
                .row("a", "b", "c");

        assertEquals(lines(
                "+---+------+-------+",
                "| a | b    | c     |",
                "+---+------+-------+"
        ), render(table, 20));
    }

    @Test
    void testRenderDoesNotChangeGrowSettings() {
        Table table = new Table().row("a", "b");
        render(table, 20);

        table.defaultGrow(3);

        assertEquals(3, table.columnGrow(0));
        assertEquals(3, table.columnGrow(1));
    }

    @Test
    void testDefaultGrowSetAfterRenderAppliesOnNextRender() {
        Table table = new Table().row("a", "b");
        String before = render(table, 20);

        table.defaultGrow(1);

        assertNotEquals(before, render(table, 20));
        assertEquals(lines(
                "+---------+--------+",
                "| a       | b      |",
                "+---------+--------+"
        ), render(table, 20));
    }

    @Test
    void testRenderGrowAllZeroDoesNotGrow() {
        Table table = new Table().defaultGrow(0).row("a", "b");

        assertEquals(lines(
                "+---+---+",
                "| a | b |",
                "+---+---+"
        ), render(table, 40));
    }

    @Test
    void testRenderGrowWidthExactlyFitsDoesNotGrow() {
        Table table = new Table().defaultGrow(1).row("a", "b");

        assertEquals(lines(
                "+---+---+",
                "| a | b |",
                "+---+---+"
        ), render(table, 9));
    }

    @Test
    void testRenderGrowWidthTooSmallKeepsNaturalWidth() {
        Table table = new Table().defaultGrow(1).row("abcdef", "b");

        assertEquals(lines(
                "+--------+---+",
                "| abcdef | b |",
                "+--------+---+"
        ), render(table, 5));
    }

    @Test
    void testRenderGrowWithAlignment() {
        Table table = new Table()
                .columnGrow(0, 1)
                .columnAlignment(0, ColumnAlign.END)
                .row("ab");

        assertEquals(lines(
                "+--------+",
                "|     ab |",
                "+--------+"
        ), render(table, 10));
    }

    @Test
    void testRenderGrowWithHeader() {
        Table table = new Table()
                .hasHeader(true)
                .columnGrow(0, 1)
                .row("h")
                .row("v");

        assertEquals(lines(
                "+------+",
                "| h    |",
                "+------+",
                "| v    |",
                "+------+"
        ), render(table, 8));
    }

    // ---------------------------------------------------------------------
    // Rendering: styles
    // ---------------------------------------------------------------------

    @Test
    void testRenderUnicodeBoxStyle() {
        Table table = nameAgeTable().hasHeader(true).style(TableStyles.UNICODE_BOX);

        assertEquals(lines(
                "┌───────┬─────┐",
                "│ Name  │ Age │",
                "├───────┼─────┤",
                "│ Alice │ 30  │",
                "└───────┴─────┘"
        ), render(table));
    }

    @Test
    void testRenderLightStyle() {
        Table table = nameAgeTable().hasHeader(true).style(TableStyles.LIGHT);

        assertEquals(lines(
                "               ",
                "  Name  | Age  ",
                "--------|------",
                "  Alice | 30   ",
                "               "
        ), render(table));
    }

    @Test
    void testRenderCustomPadding() {
        TableStyle wide = new DefaultTableStyle() {
            @Override
            public int paddingLeft() { return 2; }
            @Override
            public int paddingRight() { return 0; }
        };

        Table table = new Table()
                .style(wide)
                .row("ab", "c")
                .row("a", "cd");

        assertEquals(lines(
                "+----+----+",
                "|  ab|  c |",
                "|  a |  cd|",
                "+----+----+"
        ), render(table));
    }

    @Test
    void testRenderCustomPaddingWithGrowAndCenter() {
        TableStyle noPadding = new DefaultTableStyle() {
            @Override
            public int paddingLeft() { return 0; }
            @Override
            public int paddingRight() { return 0; }
        };

        Table table = new Table()
                .style(noPadding)
                .columnGrow(0, 1)
                .columnAlignment(0, ColumnAlign.CENTER)
                .row("ab");

        assertEquals(lines(
                "+------+",
                "|  ab  |",
                "+------+"
        ), render(table, 8));
    }

    @Test
    void testRenderBorderColor() {
        TableStyle red = new DefaultTableStyle().withBorderColor(AnsiColor.RED);
        Table table = new Table().style(red).row("a");

        AnsiRenderer ansi = new AnsiRenderer();
        TestPrinter printer = new TestPrinter(ansi);
        table.render(printer, 0, 24);
        String output = printer.getOutput();

        String redCorner = ansi.render(StyledText.text("+").fg(AnsiColor.RED));
        String redVertical = ansi.render(StyledText.text("|").fg(AnsiColor.RED));
        String plainCell = ansi.render(StyledText.text("a"));

        assertTrue(output.startsWith(redCorner));
        assertTrue(output.contains(redVertical));
        assertTrue(output.contains(plainCell));
    }

    @Test
    void testRenderKeepsCellStyles() {
        StyledText bold = StyledText.text("bold", new Style().bold());
        Table table = new Table().row(List.of(bold));

        AnsiRenderer ansi = new AnsiRenderer();
        TestPrinter printer = new TestPrinter(ansi);
        table.render(printer, 0, 24);

        assertTrue(printer.getOutput().contains(ansi.render(bold)));
    }

    // ---------------------------------------------------------------------
    // Wrapping
    // ---------------------------------------------------------------------

    @Test
    void testWrapIsOffByDefault() {
        assertFalse(new Table().wrap());
        assertTrue(new Table().wrap(true).wrap());
    }

    @Test
    void testWrapDoesNothingWhenTableFits() {
        assertEquals(render(nameAgeTable(), 80), render(nameAgeTable().wrap(true), 80));
    }

    @Test
    void testWrapShrinksWidestColumnAndWrapsCells() {
        Table table = new Table()
                .row("Name", "Description")
                .row("Alice", "likes long walks")
                .wrap(true);

        assertEquals(lines(
                "+-------+------------+",
                "| Name  | Descriptio |",
                "|       | n          |",
                "| Alice | likes long |",
                "|       | walks      |",
                "+-------+------------+"
        ), render(table, 22));
    }

    @Test
    void testWrapKeepsLineBreaksInCells() {
        Table table = new Table()
                .row("a\nbb", "c")
                .wrap(true);

        assertEquals(lines(
                "+----+---+",
                "| a  | c |",
                "| bb |   |",
                "+----+---+"
        ), render(table, 80));
    }

    @Test
    void testWrapRespectsAlignment() {
        Table table = new Table()
                .row("aaa bb")
                .columnAlignment(0, ColumnAlign.END)
                .wrap(true);

        assertEquals(lines(
                "+-----+",
                "| aaa |",
                "|  bb |",
                "+-----+"
        ), render(table, 7));
    }

    @Test
    void testWrapNeverShrinksBelowOneCharacter() {
        Table table = new Table()
                .row("ab", "cd")
                .wrap(true);

        assertEquals(lines(
                "+---+---+",
                "| a | c |",
                "| b | d |",
                "+---+---+"
        ), render(table, 0));
    }

    @Test
    void testWrapWithHeaderSeparator() {
        Table table = new Table()
                .row("Title")
                .row("one two")
                .hasHeader(true)
                .wrap(true);

        String out = render(table, 9);
        assertEquals(lines(
                "+-------+",
                "| Title |",
                "+-------+",
                "| one   |",
                "| two   |",
                "+-------+"
        ), out);
    }
}
