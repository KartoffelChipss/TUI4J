package org.strassburger.tui4j.command.exceptions;

public class TooManyArgumentsException extends CommandException {
    public TooManyArgumentsException() {
        super("Too many arguments provided");
    }
}
