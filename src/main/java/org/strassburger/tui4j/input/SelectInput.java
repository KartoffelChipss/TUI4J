package org.strassburger.tui4j.input;

import org.strassburger.tui4j.formatting.Printer;
import org.strassburger.tui4j.formatting.TextFormatter;
import org.strassburger.tui4j.input.exceptions.InputValidationException;

import java.util.ArrayList;
import java.util.List;

public class SelectInput extends Input<String> {
    private final List<String> options;
    private OptionsStyle optionsStyle = OptionsStyle.DOTS;

    public SelectInput() {
        super();
        options = new ArrayList<>();
    }

    public SelectInput(List<String> options) {
        super();
        this.options = options;
    }

    public SelectInput(String... options) {
        super();
        this.options = List.of(options);
    }

    public String read() throws InputValidationException {
        try {
            System.out.print(getLabel() + "\n");
            for (int i = 0; i < options.size(); i++) {
                switch (optionsStyle) {
                    case NONE:
                        Printer.println(" &7" + (i + 1) + " &r" + options.get(i));
                        break;
                    case DOTS:
                        Printer.println(" &7" + (i + 1) + ". &r" + options.get(i));
                        break;
                    case HYPHENS:
                        Printer.println(" &7" + (i + 1) + " - &r" + options.get(i));
                        break;
                    case BRACKETS:
                        Printer.println(" &7(" + (i + 1) + ") &r" + options.get(i));
                        break;
                    case SINGLE_BRACKETS:
                        Printer.println(" &7" + (i + 1) + ") &r" + options.get(i));
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

            return options.get(selectedIndex);
        } catch (NumberFormatException e) {
            if (isRetryOnInvalid()) {
                Printer.println(getErrorMessage());
                return read();
            } else {
                throw new InputValidationException("Invalid input.");
            }
        }
    }

    public SelectInput addOptions(String... options) {
        this.options.addAll(List.of(options));
        return this;
    }

    public SelectInput addOptions(List<String> options) {
        this.options.addAll(options);
        return this;
    }

    public SelectInput setLabel(String label) {
        super.setLabel(label);
        return this;
    }

    public SelectInput setRetryOnInvalid(boolean retryOnInvalid) {
        super.setRetryOnInvalid(retryOnInvalid);
        return this;
    }

    public SelectInput setOptionsStyle(OptionsStyle optionsStyle) {
        this.optionsStyle = optionsStyle;
        return this;
    }

    @Override
    public SelectInput setErrorMessage(String errorMessage) {
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
}