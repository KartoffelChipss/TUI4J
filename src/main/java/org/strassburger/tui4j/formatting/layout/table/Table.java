package org.strassburger.tui4j.formatting.layout.table;

import org.jspecify.annotations.Nullable;
import org.strassburger.tui4j.formatting.StyledText;
import org.strassburger.tui4j.formatting.layout.Renderable;
import org.strassburger.tui4j.formatting.layout.table.styles.ColumnAlign;
import org.strassburger.tui4j.formatting.layout.table.styles.TableStyle;
import org.strassburger.tui4j.formatting.layout.table.styles.TableStyles;
import org.strassburger.tui4j.console.Printer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A table layout component that arranges data in rows and columns with customizable styles and alignments.
 */
public class Table implements Renderable {
    private List<List<StyledText>> rows;
    private final Map<Integer, ColumnAlign> columnAlignments;
    private final Map<Integer, Integer> columnGrow;
    private boolean hasHeader;
    private TableStyle style;
    private @Nullable Integer defaultGrow = null;
    private boolean wrap = true;

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
    public Table rows(List<List<StyledText>> rows) {
        this.rows = new ArrayList<>(rows);
        return this;
    }

    public List<List<StyledText>> rows() {
        return rows;
    }

    /**
     * Adds a row to the table
     * @param row the row to add
     * @return the Table instance for chaining
     */
    public Table row(List<StyledText> row) {
        this.rows.add(row);
        return this;
    }

    /**
     * Sets a row to the table
     * @param row the row to add
     * @return the Table instance for chaining
     */
    public Table stringRow(List<String> row) {
        this.rows.add(row.stream()
                .map(StyledText::text)
                .collect(Collectors.toList()));
        return this;
    }

    /**
     * Adds a row to the table
     * @param cells the row of cells to add
     * @return the Table instance for chaining
     */
    public Table row(String... cells) {
        List<StyledText> row = new ArrayList<>(cells.length);
        for (String cell : cells) row.add(StyledText.text(cell));
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
    public Table hasHeader(boolean hasHeader) {
        this.hasHeader = hasHeader;
        return this;
    }

    /**
     * Sets the alignment for a specific column
     * @param col the column index
     * @param columnAlignments the alignment to set
     * @return the Table instance for chaining
     */
    public Table columnAlignment(int col, ColumnAlign columnAlignments) {
        this.columnAlignments.put(col, columnAlignments);
        return this;
    }

    /**
     * Gets the alignment of a specific column
     * @param col the column index
     * @return the alignment of the column ({@link ColumnAlign#START} if not set)
     */
    public ColumnAlign columnAlignment(int col) {
        return columnAlignments.getOrDefault(col, ColumnAlign.START);
    }

    /**
     * Sets the grow factor for a specific column
     * @param col the column index
     * @param grow the grow factor (must be >= 0)
     * @return the Table instance for chaining
     */
    public Table columnGrow(int col, int grow) {
        if (grow < 0) throw new IllegalArgumentException("grow must be greater than 0");
        this.columnGrow.put(col, grow);
        return this;
    }

    /**
     * Gets the grow factor of a specific column
     * @param col the column index
     * @return the grow factor of the column (the default grow, or 0 if neither is set)
     */
    public int columnGrow(int col) {
        Integer grow = columnGrow.get(col);
        if (grow != null) return grow;
        return defaultGrow != null ? defaultGrow : 0;
    }

    /**
     * Sets the default grow factor for all columns
     * @param grow the default grow factor (must be >= 0)
     * @return the Table instance for chaining
     */
    public Table defaultGrow(int grow) {
        if (grow < 0) throw new IllegalArgumentException("grow must be >= 0");
        this.defaultGrow = grow;
        return this;
    }

    public @Nullable Integer defaultGrow() {
        return defaultGrow;
    }

    /**
     * Sets the style of the table
     * @param style the {@link TableStyle} to use (e.g. {@link TableStyles#DEFAULT})
     * @return the Table instance for chaining
     */
    public Table style(TableStyle style) {
        this.style = style;
        return this;
    }

    public TableStyle style() {
        return style;
    }

    /**
     * <p>
     * Sets whether cell text is wrapped.
     * </p>
     * <p>
     * When enabled, line breaks ({@code \n}) in cells are respected, and if the table is wider than
     * the available width, the widest columns are shrunk and their cells are wrapped onto multiple lines.
     * </p>
     * @param wrap true to wrap cell text
     * @return the Table instance for chaining
     */
    public Table wrap(boolean wrap) {
        this.wrap = wrap;
        return this;
    }

    public boolean wrap() {
        return wrap;
    }

    @Override
    public void render(Printer printer, int width, int height) {
        if (rows.isEmpty()) return;


        int columns = rows.stream()
                .mapToInt(List::size)
                .max()
                .orElse(0);

        int[] colWidths = computeColumnWidths(columns);

        if (wrap) shrinkToFit(colWidths, columns, width);
        applyGrow(colWidths, columns, width);

        renderTopBorder(printer, colWidths, columns);
        renderRows(printer, colWidths, columns);
        renderBottomBorder(printer, colWidths, columns);
    }

    private int[] computeColumnWidths(int columns) {
        int[] colWidths = new int[columns];

        for (List<StyledText> row : rows) {
            for (int c = 0; c < row.size(); c++) {
                int len = wrap ? longestLine(row.get(c)) : row.get(c).length();
                colWidths[c] = Math.max(colWidths[c], len);
            }
        }

        for (int i = 0; i < colWidths.length; i++) {
            colWidths[i] += style.paddingLeft() + style.paddingRight();
        }

        return colWidths;
    }

    private static int longestLine(StyledText text) {
        int max = 0;
        for (String line : text.plainText().split("\n", -1)) {
            max = Math.max(max, line.length());
        }
        return max;
    }

    /**
     * Shrinks the widest columns until the table fits into the given width
     * (a column never gets narrower than one character of content)
     */
    private void shrinkToFit(int[] colWidths, int columns, int totalWidth) {
        int padding = style.paddingLeft() + style.paddingRight();
        int borderWidth = 1 + (columns - 1) + 1;

        int total = borderWidth;
        for (int w : colWidths) total += w;

        while (total > totalWidth) {
            int widest = -1;
            for (int c = 0; c < columns; c++) {
                if (colWidths[c] - padding > 1 && (widest == -1 || colWidths[c] > colWidths[widest])) {
                    widest = c;
                }
            }
            if (widest == -1) return; // cannot shrink any further

            colWidths[widest]--;
            total--;
        }
    }

    private void applyGrow(int[] colWidths, int columns, int totalWidth) {
        int totalMinWidth = 0;
        for (int w : colWidths) totalMinWidth += w;

        int borderWidth = 1 + (columns - 1) + 1;
        int available = totalWidth - totalMinWidth - borderWidth;

        if (available <= 0) return;

        int totalGrow = 0;
        for (int c = 0; c < columns; c++) {
            totalGrow += columnGrow(c);
        }

        if (totalGrow == 0) return;

        int remaining = available;

        for (int c = 0; c < columns; c++) {
            int grow = columnGrow(c);
            if (grow > 0) {
                int extra = (available * grow) / totalGrow;
                colWidths[c] += extra;
                remaining -= extra;
            }
        }

        // rounding leftovers
        for (int c = 0; remaining > 0 && c < columns; c++) {
            if (columnGrow(c) > 0) {
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
            int[] colWidths,
            int columns
    ) {
        for (int r = 0; r < rows.size(); r++) {
            List<List<StyledText>> cellLines = new ArrayList<>(columns);
            int rowHeight = 1;
            for (int c = 0; c < columns; c++) {
                List<StyledText> lines = cellLines(r, c, colWidths[c]);
                cellLines.add(lines);
                rowHeight = Math.max(rowHeight, lines.size());
            }

            for (int line = 0; line < rowHeight; line++) {
                printer.print(border(style.verticalLeft()));

                for (int c = 0; c < columns; c++) {
                    List<StyledText> lines = cellLines.get(c);
                    StyledText text = line < lines.size() ? lines.get(line) : StyledText.text("");
                    renderCell(printer, text, c, colWidths[c]);
                    if (c < columns - 1) printer.print(border(style.vertical()));
                }

                printer.print(border(style.verticalRight()));
                printer.println();
            }

            if (hasHeader && r == 0) {
                renderHeaderSeparator(printer, colWidths, columns);
            }
        }
    }

    /**
     * Returns the lines of a cell (a single line unless wrapping is enabled)
     */
    private List<StyledText> cellLines(int row, int col, int colWidth) {
        StyledText cell = col < rows.get(row).size()
                ? rows.get(row).get(col)
                : StyledText.text("");

        if (!wrap) return List.of(cell);

        int contentWidth = Math.max(1, colWidth - style.paddingLeft() - style.paddingRight());
        return cell.wrap(contentWidth);
    }

    private void renderCell(
            Printer printer,
            StyledText cell,
            int col,
            int colWidth
    ) {
        int cellLen = cell.length();
        int contentWidth = colWidth - style.paddingLeft() - style.paddingRight();
        int padding = Math.max(0, contentWidth - cellLen);

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
