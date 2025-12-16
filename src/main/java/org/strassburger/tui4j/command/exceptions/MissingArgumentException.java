package org.strassburger.tui4j.command.exceptions;

public class MissingArgumentException extends CommandException {
    public MissingArgumentException(String argument) {
        super("Missing required argument: " + argument);
    }
}
