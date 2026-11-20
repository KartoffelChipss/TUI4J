package org.strassburger.tui4j.formatting.layout.table.styles;

import org.junit.jupiter.api.Test;
import org.strassburger.tui4j.formatting.ansi.AnsiColor;

import static org.junit.jupiter.api.Assertions.*;

class TableStylesTest {

    @Test
    void testPresetTypes() {
        assertInstanceOf(DefaultTableStyle.class, TableStyles.DEFAULT);
        assertInstanceOf(UnicodeBoxTableStyle.class, TableStyles.UNICODE_BOX);
        assertInstanceOf(LightTableStyle.class, TableStyles.LIGHT);
    }

    @Test
    void testDefaultTableStyle() {
        TableStyle s = new DefaultTableStyle();

        assertEquals('|', s.verticalLeft());
        assertEquals('|', s.verticalRight());
        assertEquals('|', s.vertical());

        assertEquals('-', s.horizontalTop());
        assertEquals('-', s.horizontalBottom());
        assertEquals('-', s.horizontalHeaderSeparator());
        assertEquals('-', s.horizontal());

        assertEquals('+', s.topLeft());
        assertEquals('+', s.topRight());
        assertEquals('+', s.bottomLeft());
        assertEquals('+', s.bottomRight());

        assertEquals('+', s.teeTop());
        assertEquals('+', s.teeBottom());
        assertEquals('+', s.teeLeft());
        assertEquals('+', s.teeRight());
        assertEquals('+', s.intersection());

        assertEquals(1, s.paddingLeft());
        assertEquals(1, s.paddingRight());
    }

    @Test
    void testUnicodeBoxTableStyle() {
        TableStyle s = new UnicodeBoxTableStyle();

        assertEquals('│', s.verticalLeft());
        assertEquals('│', s.verticalRight());
        assertEquals('│', s.vertical());

        assertEquals('─', s.horizontalTop());
        assertEquals('─', s.horizontalBottom());
        assertEquals('─', s.horizontalHeaderSeparator());
        assertEquals('─', s.horizontal());

        assertEquals('┌', s.topLeft());
        assertEquals('┐', s.topRight());
        assertEquals('└', s.bottomLeft());
        assertEquals('┘', s.bottomRight());

        assertEquals('┬', s.teeTop());
        assertEquals('┴', s.teeBottom());
        assertEquals('├', s.teeLeft());
        assertEquals('┤', s.teeRight());
        assertEquals('┼', s.intersection());

        assertEquals(1, s.paddingLeft());
        assertEquals(1, s.paddingRight());
    }

    @Test
    void testLightTableStyle() {
        TableStyle s = new LightTableStyle();

        assertEquals(' ', s.verticalLeft());
        assertEquals(' ', s.verticalRight());
        assertEquals('|', s.vertical());

        assertEquals(' ', s.horizontalTop());
        assertEquals(' ', s.horizontalBottom());
        assertEquals('-', s.horizontalHeaderSeparator());
        assertEquals('-', s.horizontal());

        assertEquals(' ', s.topLeft());
        assertEquals(' ', s.topRight());
        assertEquals(' ', s.bottomLeft());
        assertEquals(' ', s.bottomRight());

        assertEquals(' ', s.teeTop());
        assertEquals(' ', s.teeBottom());
        assertEquals('-', s.teeLeft());
        assertEquals('-', s.teeRight());
        assertEquals('|', s.intersection());

        assertEquals(1, s.paddingLeft());
        assertEquals(1, s.paddingRight());
    }

    @Test
    void testDefaultBorderColor() {
        assertSame(AnsiColor.DEFAULT, new DefaultTableStyle().borderColor());
        assertSame(AnsiColor.DEFAULT, new UnicodeBoxTableStyle().borderColor());
        assertSame(AnsiColor.DEFAULT, new LightTableStyle().borderColor());
    }

    @Test
    void testWithBorderColor() {
        DefaultTableStyle style = new DefaultTableStyle();
        TableStyle result = style.withBorderColor(AnsiColor.RED);

        assertSame(style, result);
        assertSame(AnsiColor.RED, style.borderColor());
    }

    @Test
    void testColumnAlignValues() {
        assertArrayEquals(
                new ColumnAlign[]{ColumnAlign.START, ColumnAlign.CENTER, ColumnAlign.END},
                ColumnAlign.values()
        );
        assertEquals(ColumnAlign.CENTER, ColumnAlign.valueOf("CENTER"));
    }
}
