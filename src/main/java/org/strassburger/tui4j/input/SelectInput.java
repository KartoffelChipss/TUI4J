package org.strassburger.tui4j.input;

import org.strassburger.tui4j.formatting.StyledText;
import org.strassburger.tui4j.formatting.ansi.AnsiColor;
import org.strassburger.tui4j.input.exceptions.InputValidationException;

import java.util.ArrayList;
import java.util.List;

/**
 * @param <T> The type of the value that will be returned when the user selects an option
 */
public class SelectInput<T> extends Input<T, SelectInput<T>> {
    private final List<Option<T>> options;
    private StyledText optionsStyle = StyledText.text(" %num%. ").fg(AnsiColor.BRIGHT_WHITE).append(StyledText.text("%label%"));

    public SelectInput() {
        super();
        options = new ArrayList<>();
    }

    public SelectInput(List<Option<T>> options) {
        super();
        this.options = options;
    }

    public T read() throws InputValidationException {
        try {
            if (getPrompt() != null) getPrinter().println(getPrompt());
            for (int i = 0; i < options.size(); i++) {
                StyledText message = optionsStyle
                        .replace("%num%", String.valueOf(i + 1))
                        .replace("%label%", options.get(i).getLabel());
                getPrinter().println(message);
            }
            getPrinter().print(getCursor());
            String input = getScanner().nextLine().trim();

            if (input.contains(".")) {
                input = input.substring(0, input.indexOf("."));
            }

            int selectedIndex = Integer.parseInt(input) - 1;

            if (selectedIndex < 0 || selectedIndex >= options.size()) {
                if (isRetryOnInvalid()) {
                    getPrinter().println(getErrorMessage());
                    return read();
                } else {
                    throw new InputValidationException(StyledText.text("Invalid input.").fg(AnsiColor.RED));
                }
            }

            return options.get(selectedIndex).getValue();
        } catch (NumberFormatException e) {
            if (isRetryOnInvalid()) {
                getPrinter().println(getErrorMessage());
                return read();
            } else {
                throw new InputValidationException(StyledText.text("Invalid input.").fg(AnsiColor.RED));
            }
        }
    }

    /**
     * Adds an option to the select input
     * @param label The label that will be displayed to the user
     * @param value The value that will be returned when the user selects this option
     * @return The select input object
     */
    public SelectInput<T> addOption(String label, T value) {
        this.options.add(new Option<>(label, value));
        return this;
    }

    /**
     * Adds multiple options to the select input
     * @param options The options to add
     * @return The select input object
     */
    public SelectInput<T> addOptions(List<Option<T>> options) {
        this.options.addAll(options);
        return this;
    }

    /**
     * Sets the style of the options
     * @param optionsStyle The style of the options (%num% will be replaced with the number of the option, %label% will be replaced with the label of the option)
     * @return The select input object
     */
    public SelectInput<T> setOptionsStyle(StyledText optionsStyle) {
        this.optionsStyle = optionsStyle;
        return this;
    }

    /**
     * Sets the style of the options
     * @param optionsStyle The style of the options (%num% will be replaced with the number of the option, %label% will be replaced with the label of the option)
     * @return The select input object
     */
    public SelectInput<T> setOptionsStyle(String optionsStyle) {
        this.optionsStyle = StyledText.text(optionsStyle);
        return this;
    }

    /**
     * Represents a select option that can be selected by the user
     */
    public static class Option<T> {
        private final String label;
        private final T value;

        /**
         * Creates a new select option
         * @param label The label that will be displayed to the user
         * @param value The value that will be returned when the user selects this option
         */
        public Option(String label, T value) {
            this.label = label;
            this.value = value;
        }

        public String getLabel() {
            return label;
        }

        public T getValue() {
            return value;
        }
    }
}