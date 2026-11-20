package org.strassburger.tui4j.formatting.layout;

import org.junit.jupiter.api.Test;
import org.strassburger.tui4j.formatting.PlainTextRenderer;
import org.strassburger.tui4j.formatting.Style;
import org.strassburger.tui4j.formatting.StyledText;
import org.strassburger.tui4j.formatting.ansi.AnsiColor;
import org.strassburger.tui4j.formatting.ansi.AnsiRenderer;
import org.strassburger.tui4j.formatting.layout.flex.FlexJustify;
import org.strassburger.tui4j.formatting.layout.flex.FlexText;
import org.strassburger.tui4j.printer.TestPrinter;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FlexTextTest {
    private final PlainTextRenderer plain = new PlainTextRenderer();

    /** Renders with a visible "." separator so padding can be asserted exactly. */
    private static String render(FlexText flex, int width) {
        TestPrinter printer = new TestPrinter();
        flex.render(printer, width, 1);
        return printer.getOutput();
    }

    private static FlexText flex(FlexJustify justify, int gap, String... parts) {
        FlexText flex = new FlexText()
                .justify(justify)
                .gap(gap)
                .separatorChar(StyledText.text("."));
        for (String part : parts) flex.part(part);
        return flex;
    }

    private List<String> plainParts(FlexText flex) {
        return flex.parts().stream().map(plain::render).toList();
    }

    // ---------------------------------------------------------------------
    // Defaults and accessors
    // ---------------------------------------------------------------------

    @Test
    void testDefaults() {
        FlexText flex = new FlexText();

        assertTrue(flex.parts().isEmpty());
        assertEquals(FlexJustify.CENTER, flex.justify());
        assertEquals(3, flex.gap());
        assertEquals(" ", plain.render(flex.separatorChar()));
    }

    @Test
    void testVarargsConstructor() {
        FlexText flex = new FlexText(StyledText.text("a"), StyledText.text("b"));

        assertEquals(List.of("a", "b"), plainParts(flex));
        assertEquals(FlexJustify.CENTER, flex.justify());
        assertEquals(3, flex.gap());
    }

    @Test
    void testVarargsConstructorSinglePart() {
        FlexText flex = new FlexText(StyledText.text("hello"));
        assertEquals(List.of("hello"), plainParts(flex));
    }

    @Test
    void testVarargsConstructorPartsListIsMutable() {
        FlexText flex = new FlexText(StyledText.text("a"));
        assertDoesNotThrow(() -> flex.part("b"));
        assertEquals(List.of("a", "b"), plainParts(flex));
    }

    @Test
    void testPartString() {
        FlexText flex = new FlexText().part("a").part("b");
        assertEquals(List.of("a", "b"), plainParts(flex));
    }

    @Test
    void testPartStyledText() {
        StyledText bold = StyledText.text("bold", new Style().bold());
        FlexText flex = new FlexText().part(bold);

        assertSame(bold, flex.parts().get(0));
    }

    @Test
    void testPartsReplacesExistingParts() {
        FlexText flex = new FlexText()
                .part("old")
                .parts(new ArrayList<>(List.of(StyledText.text("new"))));

        assertEquals(List.of("new"), plainParts(flex));
    }

    @Test
    void testPartsCopiesTheGivenList() {
        List<StyledText> source = new ArrayList<>(List.of(StyledText.text("a")));
        FlexText flex = new FlexText().parts(source);
        flex.part("b");

        assertEquals(1, source.size());
        assertEquals(2, flex.parts().size());
    }

    @Test
    void testPartsWithImmutableListAllowsAddingParts() {
        FlexText flex = new FlexText().parts(List.of(StyledText.text("a")));

        assertDoesNotThrow(() -> flex.part("b"));
    }

    @Test
    void testJustify() {
        for (FlexJustify justify : FlexJustify.values()) {
            assertEquals(justify, new FlexText().justify(justify).justify());
        }
    }

    @Test
    void testGap() {
        assertEquals(7, new FlexText().gap(7).gap());
    }

    @Test
    void testSeparatorChar() {
        StyledText sep = StyledText.text("-");
        assertSame(sep, new FlexText().separatorChar(sep).separatorChar());
    }

    @Test
    void testSettersReturnSameInstance() {
        FlexText flex = new FlexText();

        assertSame(flex, flex.parts(new ArrayList<>()));
        assertSame(flex, flex.part("a"));
        assertSame(flex, flex.part(StyledText.text("b")));
        assertSame(flex, flex.justify(FlexJustify.END));
        assertSame(flex, flex.gap(1));
        assertSame(flex, flex.separatorChar(StyledText.text("-")));
    }

    // ---------------------------------------------------------------------
    // START
    // ---------------------------------------------------------------------

    @Test
    void testStartFillsWidth() {
        assertEquals("ab.cd.....", render(flex(FlexJustify.START, 1, "ab", "cd"), 10));
    }

    @Test
    void testStartUsesGap() {
        assertEquals("a...b.....", render(flex(FlexJustify.START, 3, "a", "b"), 10));
    }

    @Test
    void testStartSinglePart() {
        assertEquals("hi....", render(flex(FlexJustify.START, 3, "hi"), 6));
    }

    @Test
    void testStartZeroGap() {
        assertEquals("abcd..", render(flex(FlexJustify.START, 0, "ab", "cd"), 6));
    }

    @Test
    void testStartOverflowPrintsContentWithoutPadding() {
        assertEquals("abc.def", render(flex(FlexJustify.START, 1, "abc", "def"), 4));
    }

    @Test
    void testStartEmptyFillsWidth() {
        assertEquals(".....", render(flex(FlexJustify.START, 3), 5));
    }

    // ---------------------------------------------------------------------
    // CENTER
    // ---------------------------------------------------------------------

    @Test
    void testCenterEvenPadding() {
        assertEquals("...ab.cd...", render(flex(FlexJustify.CENTER, 1, "ab", "cd"), 11));
    }

    @Test
    void testCenterOddPaddingPutsExtraOnTheRight() {
        assertEquals("..ab.cd...", render(flex(FlexJustify.CENTER, 1, "ab", "cd"), 10));
    }

    @Test
    void testCenterSinglePart() {
        assertEquals("..hi..", render(flex(FlexJustify.CENTER, 3, "hi"), 6));
    }

    @Test
    void testCenterOverflowPrintsContentWithoutPadding() {
        assertEquals("abc.def", render(flex(FlexJustify.CENTER, 1, "abc", "def"), 4));
    }

    @Test
    void testCenterEmptyFillsWidth() {
        assertEquals(".....", render(flex(FlexJustify.CENTER, 3), 5));
    }

    // ---------------------------------------------------------------------
    // END
    // ---------------------------------------------------------------------

    @Test
    void testEndFillsWidth() {
        assertEquals(".....ab.cd", render(flex(FlexJustify.END, 1, "ab", "cd"), 10));
    }

    @Test
    void testEndSinglePart() {
        assertEquals("....hi", render(flex(FlexJustify.END, 3, "hi"), 6));
    }

    @Test
    void testEndOverflowPrintsContentWithoutPadding() {
        assertEquals("abc.def", render(flex(FlexJustify.END, 1, "abc", "def"), 4));
    }

    @Test
    void testEndEmptyFillsWidth() {
        assertEquals(".....", render(flex(FlexJustify.END, 3), 5));
    }

    // ---------------------------------------------------------------------
    // SPACE_BETWEEN
    // ---------------------------------------------------------------------

    @Test
    void testSpaceBetweenTwoParts() {
        assertEquals("a....b", render(flex(FlexJustify.SPACE_BETWEEN, 3, "a", "b"), 6));
    }

    @Test
    void testSpaceBetweenThreeParts() {
        assertEquals("a...b...c", render(flex(FlexJustify.SPACE_BETWEEN, 3, "a", "b", "c"), 9));
    }

    @Test
    void testSpaceBetweenIgnoresGap() {
        assertEquals(
                render(flex(FlexJustify.SPACE_BETWEEN, 0, "a", "b"), 6),
                render(flex(FlexJustify.SPACE_BETWEEN, 10, "a", "b"), 6)
        );
    }

    @Test
    void testSpaceBetweenRemainderIsDropped() {
        // 7 free columns over 2 gaps: each gap gets 3, the remaining column is not printed
        assertEquals("a...b...c", render(flex(FlexJustify.SPACE_BETWEEN, 3, "a", "b", "c"), 10));
    }

    @Test
    void testSpaceBetweenSinglePartIsNotPadded() {
        assertEquals("a", render(flex(FlexJustify.SPACE_BETWEEN, 3, "a"), 5));
    }

    @Test
    void testSpaceBetweenOverflowJoinsParts() {
        assertEquals("abcdef", render(flex(FlexJustify.SPACE_BETWEEN, 3, "abc", "def"), 4));
    }

    @Test
    void testSpaceBetweenEmpty() {
        assertEquals("", render(flex(FlexJustify.SPACE_BETWEEN, 3), 5));
    }

    // ---------------------------------------------------------------------
    // SPACE_AROUND
    // ---------------------------------------------------------------------

    @Test
    void testSpaceAroundTwoParts() {
        assertEquals("..a....b..", render(flex(FlexJustify.SPACE_AROUND, 3, "a", "b"), 10));
    }

    @Test
    void testSpaceAroundSinglePart() {
        assertEquals("..a..", render(flex(FlexJustify.SPACE_AROUND, 3, "a"), 5));
    }

    @Test
    void testSpaceAroundRemainderIsDropped() {
        // 9 free columns over 4 half-gaps: each gets 2, the remaining column is not printed
        assertEquals("..a....b..", render(flex(FlexJustify.SPACE_AROUND, 3, "a", "b"), 11));
    }

    @Test
    void testSpaceAroundOverflow() {
        assertEquals("abcdef", render(flex(FlexJustify.SPACE_AROUND, 3, "abc", "def"), 4));
    }

    @Test
    void testSpaceAroundEmpty() {
        assertEquals("", render(flex(FlexJustify.SPACE_AROUND, 3), 5));
    }

    // ---------------------------------------------------------------------
    // SPACE_EVENLY
    // ---------------------------------------------------------------------

    @Test
    void testSpaceEvenlyTwoParts() {
        assertEquals("..a..b..", render(flex(FlexJustify.SPACE_EVENLY, 3, "a", "b"), 8));
    }

    @Test
    void testSpaceEvenlySinglePart() {
        assertEquals("..a..", render(flex(FlexJustify.SPACE_EVENLY, 3, "a"), 5));
    }

    @Test
    void testSpaceEvenlyRemainderIsDropped() {
        // 7 free columns over 3 gaps: each gets 2, the remaining column is not printed
        assertEquals("..a..b..", render(flex(FlexJustify.SPACE_EVENLY, 3, "a", "b"), 9));
    }

    @Test
    void testSpaceEvenlyOverflow() {
        assertEquals("abcdef", render(flex(FlexJustify.SPACE_EVENLY, 3, "abc", "def"), 4));
    }

    @Test
    void testSpaceEvenlyEmptyFillsWidth() {
        assertEquals("....", render(flex(FlexJustify.SPACE_EVENLY, 3), 4));
    }

    // ---------------------------------------------------------------------
    // Shared rendering behavior
    // ---------------------------------------------------------------------

    @Test
    void testOutputWidthMatchesWidthWhenContentFits() {
        for (FlexJustify justify : List.of(FlexJustify.START, FlexJustify.CENTER, FlexJustify.END)) {
            String output = render(flex(justify, 2, "ab", "cd", "ef"), 20);
            assertEquals(20, output.length(), justify + " rendered \"" + output + "\"");
        }
    }

    @Test
    void testPartsKeepTheirOrder() {
        for (FlexJustify justify : FlexJustify.values()) {
            String output = render(flex(justify, 1, "one", "two", "three"), 30);
            int one = output.indexOf("one");
            int two = output.indexOf("two");
            int three = output.indexOf("three");
            assertTrue(one >= 0 && one < two && two < three, justify + " rendered \"" + output + "\"");
        }
    }

    @Test
    void testDefaultSeparatorIsSpace() {
        FlexText flex = new FlexText().justify(FlexJustify.END).gap(1).part("a").part("b");
        assertEquals("  a b", render(flex, 5));
    }

    @Test
    void testRenderViaPrinterUsesPrinterWidth() {
        TestPrinter printer = new TestPrinter();
        printer.setWidth(6);
        printer.print(flex(FlexJustify.SPACE_BETWEEN, 3, "a", "b"));

        assertEquals("a....b", printer.getOutput());
    }

    @Test
    void testRenderIgnoresHeight() {
        FlexText flex = flex(FlexJustify.SPACE_BETWEEN, 3, "a", "b");

        TestPrinter small = new TestPrinter();
        flex.render(small, 6, 1);
        TestPrinter large = new TestPrinter();
        flex.render(large, 6, 100);

        assertEquals(small.getOutput(), large.getOutput());
    }

    @Test
    void testRenderUsesPlainTextLengthForStyledParts() {
        FlexText flex = new FlexText()
                .justify(FlexJustify.SPACE_BETWEEN)
                .separatorChar(StyledText.text("."))
                .part(StyledText.text("ab", new Style().bold()).append("cd"))
                .part("e");

        assertEquals("abcd...e", render(flex, 8));
    }

    @Test
    void testRenderKeepsPartAndSeparatorStyles() {
        AnsiRenderer ansi = new AnsiRenderer();
        StyledText part = StyledText.text("a").fg(AnsiColor.GREEN);
        StyledText sep = StyledText.text(".").fg(AnsiColor.RED);

        FlexText flex = new FlexText()
                .justify(FlexJustify.SPACE_BETWEEN)
                .separatorChar(sep)
                .part(part)
                .part("b");

        TestPrinter printer = new TestPrinter(ansi);
        flex.render(printer, 3, 1);
        String output = printer.getOutput();

        assertTrue(output.startsWith(ansi.render(part)));
        assertTrue(output.contains(ansi.render(sep)));
        assertTrue(output.endsWith(ansi.render(StyledText.text("b"))));
    }

    @Test
    void testRenderCanBeRepeated() {
        FlexText flex = flex(FlexJustify.CENTER, 1, "a", "b");
        assertEquals(render(flex, 10), render(flex, 10));
    }

    // ---------------------------------------------------------------------
    // keyValue
    // ---------------------------------------------------------------------

    @Test
    void testKeyValueWithStrings() {
        FlexText flex = FlexText.keyValue("Name", "Bob");

        assertEquals(FlexJustify.SPACE_BETWEEN, flex.justify());
        assertEquals(List.of("Name", "Bob"), plainParts(flex));
        assertEquals("⋅", plain.render(flex.separatorChar()));
    }

    @Test
    void testKeyValueWithStringsUsesBrightWhite() {
        AnsiRenderer ansi = new AnsiRenderer();
        FlexText flex = FlexText.keyValue("Name", "Bob");

        assertEquals(ansi.render(StyledText.text("Name").fg(AnsiColor.BRIGHT_WHITE)), ansi.render(flex.parts().get(0)));
        assertEquals(ansi.render(StyledText.text("Bob").fg(AnsiColor.BRIGHT_WHITE)), ansi.render(flex.parts().get(1)));
    }

    @Test
    void testKeyValueSeparatorIsBrightBlack() {
        AnsiRenderer ansi = new AnsiRenderer();
        FlexText flex = FlexText.keyValue("Name", "Bob");

        assertEquals(ansi.render(StyledText.text("⋅").fg(AnsiColor.BRIGHT_BLACK)), ansi.render(flex.separatorChar()));
    }

    @Test
    void testKeyValueWithStyledTextKeepsStyles() {
        StyledText key = StyledText.text("Key").fg(AnsiColor.CYAN);
        StyledText value = StyledText.text("Value").bold();
        FlexText flex = FlexText.keyValue(key, value);

        assertSame(key, flex.parts().get(0));
        assertSame(value, flex.parts().get(1));
        assertEquals(FlexJustify.SPACE_BETWEEN, flex.justify());
    }

    @Test
    void testKeyValueRendersAcrossFullWidth() {
        assertEquals("Name⋅⋅⋅⋅⋅Bob", render(FlexText.keyValue("Name", "Bob"), 12));
    }

    @Test
    void testKeyValueCanBeExtended() {
        FlexText flex = FlexText.keyValue("a", "b").part("c");
        assertEquals(3, flex.parts().size());
    }

    // ---------------------------------------------------------------------
    // Wrapping
    // ---------------------------------------------------------------------

    private static String renderLines(FlexText flex, int width) {
        return render(flex, width).replace(System.lineSeparator(), "\n");
    }

    @Test
    void testWrapIsOffByDefault() {
        assertFalse(new FlexText().wrap());
        assertTrue(new FlexText().wrap(true).wrap());
    }

    @Test
    void testWrapDoesNothingWhenContentFits() {
        FlexText flex = flex(FlexJustify.START, 1, "aa", "bb").wrap(true);
        assertEquals("aa.bb.....", renderLines(flex, 10));
    }

    @Test
    void testWrapOffOverflowsOnOneLine() {
        FlexText flex = flex(FlexJustify.START, 1, "aaa", "bbb", "ccc");
        assertEquals("aaa.bbb.ccc", renderLines(flex, 7));
    }

    @Test
    void testWrapKeepsPartsNextToEachOther() {
        FlexText flex = flex(FlexJustify.START, 1, "ab", "one two three").wrap(true);
        assertEquals("ab.one two\n  .three  ", renderLines(flex, 10));
    }

    @Test
    void testWrapShrinksTheWidestPart() {
        FlexText flex = flex(FlexJustify.START, 1, "aaaa bbbb", "cc dd").wrap(true);
        assertEquals("aaaa.cc dd\nbbbb.     ", renderLines(flex, 10));
    }

    @Test
    void testWrapShrinksMultiplePartsEvenly() {
        FlexText flex = flex(FlexJustify.START, 1, "aaa bbb", "ccc ddd").wrap(true);
        assertEquals("aaa.ccc\nbbb.ddd", renderLines(flex, 7));
    }

    @Test
    void testWrapColumnsStayAlignedWithSpaceEvenly() {
        FlexText flex = flex(FlexJustify.SPACE_EVENLY, 1, "x", "aaaa bb").wrap(true);
        assertEquals(".x.aaaa.\n. .bb  .", renderLines(flex, 8));
    }

    @Test
    void testWrapSplitsWordsWiderThanTheColumn() {
        FlexText flex = flex(FlexJustify.START, 1, "a", "bcdef").wrap(true);
        assertEquals("a.bcd\n .ef ", renderLines(flex, 5));
    }

    @Test
    void testWrapOverflowsWhenPartsCannotShrinkFurther() {
        FlexText flex = flex(FlexJustify.START, 1, "ab", "cd").wrap(true);
        assertEquals("a.c\nb.d", renderLines(flex, 2));
    }

    @Test
    void testWrapKeepsStyles() {
        TestPrinter printer = new TestPrinter(new AnsiRenderer());
        new FlexText()
                .part(StyledText.text("aaa").bold())
                .part(StyledText.text("bbb").bold())
                .justify(FlexJustify.START)
                .gap(1)
                .wrap(true)
                .render(printer, 5, 1);

        String out = printer.getOutput();
        assertEquals(2, out.split(java.util.regex.Pattern.quote(System.lineSeparator())).length);
        assertTrue(out.contains("aa"));
        assertTrue(out.contains("\u001B[1m"));
    }
}
