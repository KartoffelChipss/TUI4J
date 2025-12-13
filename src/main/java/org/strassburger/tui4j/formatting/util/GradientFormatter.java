package org.strassburger.tui4j.formatting.util;

import org.strassburger.colorlab4j.color.Color;
import org.strassburger.colorlab4j.gradients.Gradient;
import org.strassburger.tui4j.formatting.TextColor;

import java.util.List;

/**
 * @deprecated
 */
public class GradientFormatter {
    public static <T extends Color, U extends Gradient<T>> String applyGradient(String text, U gradient) {
        List<T> colors = gradient.getColors(text.length());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            T color = colors.get(i);
            sb.append(color.toAnsi());
            sb.append(text.charAt(i));
        }

        sb.append(TextColor.RESET);
        return sb.toString();
    }
}
