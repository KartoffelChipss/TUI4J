package org.strassburger.tui4j.formatting;

import org.junit.jupiter.api.Test;
import org.strassburger.colorlab4j.color.spaces.RGBColor;

import static org.junit.jupiter.api.Assertions.*;

class StyledTextTest {

    @Test
    void testSimpleTextCreation() {
        StyledText text = StyledText.text("Hello");
        assertEquals(1, text.getSpans().size());
        assertEquals("Hello", text.getSpans().get(0).getText());
        assertNotNull(text.getSpans().get(0).getStyle());
    }

    @Test
    void testStyledTextCreation() {
        Style style = new Style().bold().fg(RGBColor.fromHex("ff0000"));
        StyledText text = StyledText.text("Hello", style);

        assertEquals(1, text.getSpans().size());
        Span span = text.getSpans().get(0);
        assertEquals("Hello", span.getText());
        assertEquals(style, span.getStyle());
    }

    @Test
    void testAppendAndPrepend() {
        StyledText t1 = StyledText.text("Hello", new Style().underline());
        StyledText t2 = StyledText.text("World");

        StyledText combined = t1.append(t2);
        assertEquals(2, combined.getSpans().size());
        assertEquals("Hello", combined.getSpans().get(0).getText());
        assertEquals("World", combined.getSpans().get(1).getText());

        StyledText prepended = t1.prepend(t2);
        assertEquals(2, prepended.getSpans().size());
        assertEquals("World", prepended.getSpans().get(0).getText());
        assertEquals("Hello", prepended.getSpans().get(1).getText());
    }

    @Test
    void testAppendStringWithStyle() {
        StyledText text = StyledText.text("Hello")
                .append(" World", new Style().bold());

        assertEquals(2, text.getSpans().size());
        assertEquals(" World", text.getSpans().get(1).getText());
        assertTrue(text.getSpans().get(1).getStyle().isBold());
    }

    @Test
    void testStyleModifiers() {
        StyledText text = StyledText.text("Hello")
                .fg(RGBColor.fromHex("00ff00"))
                .bg(RGBColor.fromHex("0000ff"))
                .bold()
                .underline()
                .reversed();

        Span span = text.getSpans().get(0);
        assertEquals("Hello", span.getText());
        assertEquals(RGBColor.fromHex("00ff00"), span.getStyle().getForeground());
        assertEquals(RGBColor.fromHex("0000ff"), span.getStyle().getBackground());
        assertTrue(span.getStyle().isBold());
        assertTrue(span.getStyle().isUnderline());
        assertTrue(span.getStyle().isInverse());
    }

    @Test
    void testSpanMerging() {
        Style red = new Style().fg(RGBColor.fromHex("ff0000"));
        StyledText t1 = StyledText.text("A", red);
        StyledText t2 = StyledText.text("B", red);
        StyledText merged = t1.append(t2);

        assertEquals(1, merged.getSpans().size());
        assertEquals("AB", merged.getSpans().get(0).getText());
    }

    @Test
    void testImmutableStyledText() {
        StyledText t1 = StyledText.text("Hello");
        StyledText t2 = t1.fg(RGBColor.fromHex("ff0000"));

        // Original should remain unchanged
        assertNull(t1.getSpans().get(0).getStyle().getForeground());
        // New StyledText has foreground applied
        assertEquals(RGBColor.fromHex("ff0000"), t2.getSpans().get(0).getStyle().getForeground());
    }
}