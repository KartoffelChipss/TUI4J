package org.strassburger.tui4j.input;

import org.strassburger.tui4j.formatting.Printer;
import org.strassburger.tui4j.formatting.TextFormatter;
import org.strassburger.tui4j.input.exceptions.InputValidationException;

import java.util.ArrayList;
import java.util.List;

public class SelectInput<T> extends Input<T> {
    private final List<Option<T>> options;
    private OptionsStyle optionsStyle = OptionsStyle.DOTS;

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
            System.out.print(getLabel() + "\n");
            for (int i = 0; i < options.size(); i++) {
                switch (optionsStyle) {
                    case NONE:
                        Printer.println(" &7" + (i + 1) + " &r" + options.get(i).getLabel());
                        break;
                    case DOTS:
                        Printer.println(" &7" + (i + 1) + ". &r" + options.get(i).getLabel());
                        break;
                    case HYPHENS:
                        Printer.println(" &7" + (i + 1) + " - &r" + options.get(i).getLabel());
                        break;
                    case BRACKETS:
                        Printer.println(" &7(" + (i + 1) + ") &r" + options.get(i).getLabel());
                        break;
                    case SINGLE_BRACKETS:
                        Printer.println(" &7" + (i + 1) + ") &r" + options.get(i).getLabel());
                        break;
                }
            }
            System.out.print(TextFormatter.format("&8> "));
            String input = getScanner().nextLine().trim();

            if (input.contains(".")) {
                input = input.substring(0, input.indexOf("."));
            }

            int selectedIndex = Integer.parseInt(input) - 1;

            if (selectedIndex < 0 || selectedIndex >= options.size()) {
                if (isRetryOnInvalid()) {
                    Printer.println(getErrorMessage());
                    return read();
                } else {
                    throw new InputValidationException("Invalid input.");
                }
            }

            return options.get(selectedIndex).getValue();
        } catch (NumberFormatException e) {
            if (isRetryOnInvalid()) {
                Printer.println(getErrorMessage());
                return read();
            } else {
                throw new InputValidationException("Invalid input.");
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

    public SelectInput<T> setLabel(String label) {
        super.setLabel(label);
        return this;
    }

    public SelectInput<T> setRetryOnInvalid(boolean retryOnInvalid) {
        super.setRetryOnInvalid(retryOnInvalid);
        return this;
    }

    public SelectInput<T> setOptionsStyle(OptionsStyle optionsStyle) {
        this.optionsStyle = optionsStyle;
        return this;
    }

    @Override
    public SelectInput<T> setErrorMessage(String errorMessage) {
        super.setErrorMessage(errorMessage);
        return this;
    }

    public enum OptionsStyle {
        /**
         * No style
         */
        NONE,
        /**
         * Dots style
         * e.g. " 1. Option Alpha"
         */
        DOTS,
        /**
         * Hyphens style
         * e.g. " 1 - Option Alpha"
         */
        HYPHENS,
        /**
         * Brackets style
         * e.g. " (1) Option Alpha"
         */
        BRACKETS,
        /**
         * Single brackets style
         * e.g. " 1) Option Alpha"
         */
        SINGLE_BRACKETS
    }

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