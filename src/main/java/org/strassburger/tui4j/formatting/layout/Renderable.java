package org.strassburger.tui4j.formatting.layout;

import org.strassburger.tui4j.printer.Printer;

/**
 * An interface for renderable layout elements
 */
public interface Renderable {
    /**
     * Render the element using the given printer and dimensions
     * @param printer the printer to use
     * @param width the available width
     * @param height the available height
     */
    void render(Printer printer, int width, int height);
}
