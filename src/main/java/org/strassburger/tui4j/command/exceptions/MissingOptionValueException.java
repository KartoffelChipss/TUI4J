package org.strassburger.tui4j.command.exceptions;

public class MissingOptionValueException extends CommandException {
    public MissingOptionValueException(String option) {
        super("Missing value for option: " + option);
    }
}
