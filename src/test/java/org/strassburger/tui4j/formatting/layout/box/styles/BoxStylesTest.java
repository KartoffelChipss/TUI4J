package org.strassburger.tui4j.formatting.layout.box.styles;

import org.junit.jupiter.api.Test;
import org.strassburger.tui4j.formatting.ansi.AnsiColor;

import static org.junit.jupiter.api.Assertions.*;

class BoxStylesTest {

    @Test
    void testPresetTypes() {
        assertInstanceOf(DefaultBoxStyle.class, BoxStyles.DEFAULT);
        assertInstanceOf(UnicodeBoxStyle.class, BoxStyles.UNICODE_BOX);
        assertInstanceOf(RoundedBoxStyle.class, BoxStyles.ROUNDED);
        assertInstanceOf(DoubleBoxStyle.class, BoxStyles.DOUBLE);
        assertInstanceOf(HeavyBoxStyle.class, BoxStyles.HEAVY);
    }

    @Test
    void testDefaultPadding() {
        BoxStyle s = new DefaultBoxStyle();
        assertEquals(1, s.paddingLeft());
        assertEquals(1, s.paddingRight());
        assertEquals(0, s.paddingTop());
        assertEquals(0, s.paddingBottom());
    }

    @Test
    void testWithBorderColorReturnsCopyOfSameType() {
        BoxStyle original = new RoundedBoxStyle();
        BoxStyle colored = original.withBorderColor(AnsiColor.BLUE);

        assertInstanceOf(RoundedBoxStyle.class, colored);
        assertNotSame(original, colored);
        assertEquals(AnsiColor.DEFAULT, original.borderColor());
        assertEquals(AnsiColor.BLUE, colored.borderColor());
        assertEquals('╭', colored.topLeft());
    }
}
