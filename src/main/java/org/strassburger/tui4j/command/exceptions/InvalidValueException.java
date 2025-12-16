package org.strassburger.tui4j.command.exceptions;

public class InvalidValueException extends CommandException {
    public InvalidValueException(String value, Class<?> type, Throwable cause) {
        super("Invalid value '" + value + "' for type " + type.getSimpleName(), cause);
    }
}
