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
}