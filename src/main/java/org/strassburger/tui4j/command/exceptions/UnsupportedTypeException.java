package org.strassburger.tui4j.command.exceptions;

public class UnsupportedTypeException extends CommandException {
    public UnsupportedTypeException(Class<?> type) {
        super("Unsupported type: " + type.getName());
    }
}
