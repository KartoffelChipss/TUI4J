package org.strassburger.tui4j.formatting.layout.table;

import org.strassburger.tui4j.formatting.PlainTextRenderer;
import org.strassburger.tui4j.formatting.StyledText;
import org.strassburger.tui4j.formatting.layout.Renderable;
import org.strassburger.tui4j.formatting.layout.table.styles.ColumnAlign;
import org.strassburger.tui4j.formatting.layout.table.styles.TableStyle;
import org.strassburger.tui4j.formatting.layout.table.styles.TableStyles;
import org.strassburger.tui4j.printer.Printer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A table layout component that arranges data in rows and columns with customizable styles and alignments.
 */
public class Table implements Renderable {
    private List<List<StyledText>> rows;
    private final Map<Integer, ColumnAlign> columnAlignments;
    private final Map<Integer, Integer> columnGrow;
    private boolean hasHeader;
    private TableStyle style;
    private Integer defaultGrow = null;

    public Table() {
        this.rows = new ArrayList<>();
        this.columnAlignments = new HashMap<>();
        this.columnGrow = new HashMap<>();
        this.hasHeader = false;
        this.style = TableStyles.DEFAULT;
    }

    /**
     * Sets the rows of the table
     * @param rows the list of rows, each row is a list of StyledText cells
     * @return the Table instance for chaining
     */
    public Table setRows(List<List<StyledText>> rows) {
        this.rows = rows;
        return this;
    }

    /**
     * Adds a row to the table
     * @param row the row to add
     * @return the Table instance for chaining
     */
    public Table addRow(List<StyledText> row) {
        this.rows.add(row);
        return this;
    }

    public boolean hasHeader() {
        return hasHeader;
    }

    /**
     * Sets whether the table has a header row
     * @param hasHeader true if the first row is a header
     * @return the Table instance for chaining
     */
    public Table setHasHeader(boolean hasHeader) {
        this.hasHeader = hasHeader;
        return this;
    }

    /**
     * Sets the alignment for a specific column
     * @param col the column index
     * @param columnAlignments the alignment to set
     * @return the Table instance for chaining
     */
    public Table setColumnAlignment(int col, ColumnAlign columnAlignments) {
        this.columnAlignments.put(col, columnAlignments);
        return this;
    }

    /**
     * Sets the grow factor for a specific column
     * @param col the column index
     * @param grow the grow factor (must be >= 0)
     * @return the Table instance for chaining
     */
    public Table setColumnGrow(int col, int grow) {
        if (grow < 0) throw new IllegalArgumentException("grow must be greater than 0");
        this.columnGrow.put(col, grow);
        return this;
    }

    /**
     * Sets the default grow factor for all columns
     * @param grow the default grow factor (must be >= 0)
     * @return the Table instance for chaining
     */
    public Table setDefaultGrow(int grow) {
        if (grow < 0) throw new IllegalArgumentException("grow must be >= 0");
        this.defaultGrow = grow;
        return this;
    }

    /**
     * Sets the style of the table
     * @param style the {@link TableStyle} to use (e.g. {@link TableStyles#DEFAULT})
     * @return the Table instance for chaining
     */
    public Table setStyle(TableStyle style) {
        this.style = style;
        return this;
    }

    @Override
    public void render(Printer printer, int width, int height) {
        if (rows.isEmpty()) return;

        PlainTextRenderer plain = new PlainTextRenderer();

        int columns = rows.stream()
                .mapToInt(List::size)
                .max()
                .orElse(0);

        resolveColumnGrow(columns);

        int[] colWidths = computeColumnWidths(plain, columns);

        applyGrow(colWidths, columns, width);

        renderTopBorder(printer, colWidths, columns);
        renderRows(printer, plain, colWidths, columns);
        renderBottomBorder(printer, colWidths, columns);
    }

    private void resolveColumnGrow(int columns) {
        for (int c = 0; c < columns; c++) {
            columnGrow.putIfAbsent(c, defaultGrow != null ? defaultGrow : 0);
        }
    }

    private int[] computeColumnWidths(PlainTextRenderer plain, int columns) {
        int[] colWidths = new int[columns];

        for (List<StyledText> row : rows) {
            for (int c = 0; c < row.size(); c++) {
                int len = plain.render(row.get(c)).length();
                colWidths[c] = Math.max(colWidths[c], len);
            }
        }

        for (int i = 0; i < colWidths.length; i++) {
            colWidths[i] += style.paddingLeft() + style.paddingRight();
        }

        return colWidths;
    }

    private void applyGrow(int[] colWidths, int columns, int totalWidth) {
        int totalMinWidth = 0;
        for (int w : colWidths) totalMinWidth += w;

        int borderWidth = 1 + (columns - 1) + 1;
        int available = totalWidth - totalMinWidth - borderWidth;

        if (available <= 0) return;

        int totalGrow = 0;
        for (int c = 0; c < columns; c++) {
            totalGrow += columnGrow.getOrDefault(c, 0);
        }

        if (totalGrow == 0) return;

        int remaining = available;

        for (int c = 0; c < columns; c++) {
            int grow = columnGrow.getOrDefault(c, 0);
            if (grow > 0) {
                int extra = (available * grow) / totalGrow;
                colWidths[c] += extra;
                remaining -= extra;
            }
        }

        // rounding leftovers
        for (int c = 0; remaining > 0 && c < columns; c++) {
            if (columnGrow.getOrDefault(c, 0) > 0) {
                colWidths[c]++;
                remaining--;
            }
        }
    }

    private void renderTopBorder(Printer printer, int[] colWidths, int columns) {
        printer.print(border(style.topLeft()));
        for (int c = 0; c < columns; c++) {
            printer.print(borderRepeat(style.horizontalTop(), colWidths[c]));
            if (c < columns - 1) {
                printer.print(border(style.teeTop()));
            }
        }
        printer.print(border(style.topRight()));
        printer.println();
    }

    private void renderRows(
            Printer printer,
            PlainTextRenderer plain,
            int[] colWidths,
            int columns
    ) {
        for (int r = 0; r < rows.size(); r++) {
            printer.print(border(style.verticalLeft()));

            for (int c = 0; c < columns; c++) {
                renderCell(printer, plain, r, c, colWidths[c]);
                if (c < columns - 1) printer.print(border(style.vertical()));
            }

            printer.print(border(style.verticalRight()));
            printer.println();

            if (hasHeader && r == 0) {
                renderHeaderSeparator(printer, colWidths, columns);
            }
        }
    }

    private void renderCell(
            Printer printer,
            PlainTextRenderer plain,
            int row,
            int col,
            int colWidth
    ) {
        StyledText cell = col < rows.get(row).size()
                ? rows.get(row).get(col)
                : StyledText.text("");

        int cellLen = plain.render(cell).length();
        int contentWidth = colWidth - style.paddingLeft() - style.paddingRight();
        int padding = contentWidth - cellLen;

        ColumnAlign align = columnAlignments.getOrDefault(col, ColumnAlign.START);

        printer.print(" ".repeat(style.paddingLeft()));

        switch (align) {
            case START -> {
                printer.print(cell);
                printer.print(" ".repeat(padding));
            }
            case END -> {
                printer.print(" ".repeat(padding));
                printer.print(cell);
            }
            case CENTER -> {
                int left = padding / 2;
                int right = padding - left;
                printer.print(" ".repeat(left));
                printer.print(cell);
                printer.print(" ".repeat(right));
            }
        }

        printer.print(" ".repeat(style.paddingRight()));
    }

    private void renderHeaderSeparator(Printer printer, int[] colWidths, int columns) {
        printer.print(border(style.teeLeft()));
        for (int c = 0; c < columns; c++) {
            printer.print(borderRepeat(style.horizontalHeaderSeparator(), colWidths[c]));
            if (c < columns - 1) {
                printer.print(border(style.intersection()));
            }
        }
        printer.print(border(style.teeRight()));
        printer.println();
    }

    private void renderBottomBorder(Printer printer, int[] colWidths, int columns) {
        printer.print(border(style.bottomLeft()));
        for (int c = 0; c < columns; c++) {
            printer.print(borderRepeat(style.horizontalBottom(), colWidths[c]));
            if (c < columns - 1) {
                printer.print(border(style.teeBottom()));
            }
        }
        printer.print(border(style.bottomRight()));
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
