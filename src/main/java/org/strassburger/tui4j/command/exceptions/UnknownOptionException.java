package org.strassburger.tui4j.command.exceptions;

public class UnknownOptionException extends CommandException {
    public UnknownOptionException(String option) {
        super("Unknown option: " + option);
    }
}
