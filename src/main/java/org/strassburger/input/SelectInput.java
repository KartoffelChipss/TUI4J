package org.strassburger.input;

import org.strassburger.formatting.TextFormatter;
import org.strassburger.input.exceptions.InputValidationException;

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

    public SelectInput addOptions(String... options) {
        this.options.addAll(List.of(options));
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

    public String read() throws InputValidationException {
        try {
            System.out.print(label + "\n");
            for (int i = 0; i < options.size(); i++) {
                switch (optionsStyle) {
                    case NONE:
                        System.out.println(" " + (i + 1) + " " + options.get(i));
                        break;
                    case DOTS:
                        System.out.println(" " + (i + 1) + ". " + options.get(i));
                        break;
                    case HYPHENS:
                        System.out.println(" " + (i + 1) + " - " + options.get(i));
                        break;
                    case BRACKETS:
                        System.out.println(" (" + (i + 1) + ") " + options.get(i));
                        break;
                    case SINGLE_BRACKETS:
                        System.out.println(" " + (i + 1) + ") " + options.get(i));
                        break;
                }
            }
            System.out.print(TextFormatter.format("&7> "));
            String input = getScanner().nextLine().trim();

            if (input.contains(".")) {
                input = input.substring(0, input.indexOf("."));
            }

            int selectedIndex = Integer.parseInt(input) - 1;

            if (selectedIndex < 0 || selectedIndex >= options.size()) {
                if (retryOnInvalid) {
                    System.out.println("Invalid input. Please try again.");
                    return read();
                } else {
                    throw new InputValidationException("Invalid input.");
                }
            }

            return options.get(selectedIndex);
        } catch (NumberFormatException e) {
            if (retryOnInvalid) {
                System.out.println("Invalid input. Please try again.");
                return read();
            } else {
                throw new InputValidationException("Invalid input.");
            }
        }
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