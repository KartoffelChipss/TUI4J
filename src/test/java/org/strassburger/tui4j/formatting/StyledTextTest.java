package org.strassburger.tui4j.formatting;

import org.junit.jupiter.api.Test;
import org.strassburger.colorlab4j.color.Color;
import org.strassburger.colorlab4j.color.spaces.RGBColor;

import static org.junit.jupiter.api.Assertions.*;

class StyledTextTest {

    @Test
    void testSimpleTextCreation() {
        StyledText text = StyledText.text("Hello");
        assertEquals(1, text.spans().size());
        assertEquals("Hello", text.spans().get(0).text());
        assertNotNull(text.spans().get(0).style());
    }

    @Test
    void testStyledTextCreation() {
        Style style = new Style().bold().fg(RGBColor.fromHex("ff0000"));
        StyledText text = StyledText.text("Hello", style);

        assertEquals(1, text.spans().size());
        Span span = text.spans().get(0);
        assertEquals("Hello", span.text());
        assertEquals(style, span.style());
    }

    @Test
    void testAppendAndPrepend() {
        StyledText t1 = StyledText.text("Hello", new Style().underline());
        StyledText t2 = StyledText.text("World");

        StyledText combined = t1.append(t2);
        assertEquals(2, combined.spans().size());
        assertEquals("Hello", combined.spans().get(0).text());
        assertEquals("World", combined.spans().get(1).text());

        StyledText prepended = t1.prepend(t2);
        assertEquals(2, prepended.spans().size());
        assertEquals("World", prepended.spans().get(0).text());
        assertEquals("Hello", prepended.spans().get(1).text());
    }

    @Test
    void testAppendStringWithStyle() {
        StyledText text = StyledText.text("Hello")
                .append(" World", new Style().bold());

        assertEquals(2, text.spans().size());
        assertEquals(" World", text.spans().get(1).text());
        assertTrue(text.spans().get(1).style().isBold());
    }

    @Test
    void testStyleModifiers() {
        StyledText text = StyledText.text("Hello")
                .fg(RGBColor.fromHex("00ff00"))
                .bg(RGBColor.fromHex("0000ff"))
                .bold()
                .underline()
                .reversed();

        Span span = text.spans().get(0);
        assertEquals("Hello", span.text());
        assertEquals(RGBColor.fromHex("00ff00"), span.style().foreground());
        assertEquals(RGBColor.fromHex("0000ff"), span.style().background());
        assertTrue(span.style().isBold());
        assertTrue(span.style().isUnderline());
        assertTrue(span.style().isInverse());
    }

    @Test
    void testSpanMerging() {
        Style red = new Style().fg(RGBColor.fromHex("ff0000"));
        StyledText t1 = StyledText.text("A", red);
        StyledText t2 = StyledText.text("B", red);
        StyledText merged = t1.append(t2);

        assertEquals(1, merged.spans().size());
        assertEquals("AB", merged.spans().get(0).text());
    }

    @Test
    void testImmutableStyledText() {
        StyledText t1 = StyledText.text("Hello");
        StyledText t2 = t1.fg(RGBColor.fromHex("ff0000"));

        // Original should remain unchanged
        assertNull(t1.spans().get(0).style().foreground());
        // New StyledText has foreground applied
        assertEquals(RGBColor.fromHex("ff0000"), t2.spans().get(0).style().foreground());
    }

    @Test
    void testReplace() {
        StyledText text = StyledText.text("Hello World");
        StyledText replaced = text.replace("World", "Java");

        assertEquals("Hello World", text.spans().get(0).text());

        assertEquals("Hello Java", replaced.spans().get(0).text());

        StyledText unchanged = text.replace("Python", "Java");
        assertEquals("Hello World", unchanged.spans().get(0).text());
    }

    @Test
    void testSubstring() {
        StyledText text = StyledText.text("Hello World");

        StyledText sub = text.substring(0, 5);
        assertEquals("Hello", sub.spans().get(0).text());

        StyledText sub2 = text.substring(6, 11);
        assertEquals("World", sub2.spans().get(0).text());

        StyledText fullSub = text.substring(0, text.spans().get(0).text().length());
        assertEquals("Hello World", fullSub.spans().get(0).text());
    }

    @Test
    void testLength() {
        StyledText text = StyledText.text("Hello").fg(Color.fromHex("ff0000"));
        assertEquals(5, text.length());

        StyledText text2 = StyledText.text("Hello").fg(Color.fromHex("ff0000")).append(" World");
        assertEquals(11, text2.length());
    }

    // ---------------------------------------------------------------------
    // Substring and wrapping
    // ---------------------------------------------------------------------

    private static java.util.List<String> plainLines(java.util.List<StyledText> lines) {
        return lines.stream().map(StyledText::plainText).toList();
    }

    @Test
    void testSubstringKeepsStyles() {
        Style red = new Style().fg(RGBColor.fromHex("ff0000"));
        StyledText text = StyledText.text("Hello ").append("World", red);

        StyledText sub = text.substring(3, 8);

        assertEquals(2, sub.spans().size());
        assertEquals("lo ", sub.spans().get(0).text());
        assertEquals("Wo", sub.spans().get(1).text());
        assertEquals(red, sub.spans().get(1).style());
    }

    @Test
    void testSubstringEmpty() {
        assertEquals(0, StyledText.text("Hello").substring(2, 2).length());
    }

    @Test
    void testSubstringOutOfRangeThrows() {
        StyledText text = StyledText.text("Hello");
        assertThrows(IndexOutOfBoundsException.class, () -> text.substring(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> text.substring(0, 6));
        assertThrows(IndexOutOfBoundsException.class, () -> text.substring(3, 2));
    }

    @Test
    void testPlainText() {
        StyledText text = StyledText.text("Hello ").bold().append("World");
        assertEquals("Hello World", text.plainText());
    }

    @Test
    void testWrapFitsOnOneLine() {
        assertEquals(java.util.List.of("Hello World"), plainLines(StyledText.text("Hello World").wrap(11)));
    }

    @Test
    void testWrapBreaksAtSpaces() {
        assertEquals(java.util.List.of("the quick", "brown fox", "jumps"),
                plainLines(StyledText.text("the quick brown fox jumps").wrap(10)));
    }

    @Test
    void testWrapDropsSpacesAtBreak() {
        assertEquals(java.util.List.of("aaa", "bbb"), plainLines(StyledText.text("aaa   bbb").wrap(4)));
    }

    @Test
    void testWrapSplitsLongWords() {
        assertEquals(java.util.List.of("abcd", "efgh", "ij"), plainLines(StyledText.text("abcdefghij").wrap(4)));
    }

    @Test
    void testWrapKeepsLineBreaks() {
        assertEquals(java.util.List.of("ab", "", "cd"), plainLines(StyledText.text("ab\n\ncd").wrap(10)));
    }

    @Test
    void testWrapEmptyText() {
        assertEquals(java.util.List.of(""), plainLines(StyledText.text("").wrap(5)));
    }

    @Test
    void testWrapKeepsStyles() {
        Style bold = new Style().bold();
        StyledText text = StyledText.text("plain ").append("bold text", bold);

        java.util.List<StyledText> lines = text.wrap(10);

        assertEquals(java.util.List.of("plain bold", "text"), plainLines(lines));
        assertEquals(bold, lines.get(0).spans().get(1).style());
        assertEquals(bold, lines.get(1).spans().get(0).style());
    }

    @Test
    void testWrapInvalidWidthThrows() {
        assertThrows(IllegalArgumentException.class, () -> StyledText.text("a").wrap(0));
    }
}
