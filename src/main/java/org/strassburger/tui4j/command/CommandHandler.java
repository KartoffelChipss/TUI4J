package org.strassburger.tui4j.command;

import org.strassburger.tui4j.command.exceptions.CommandException;

@FunctionalInterface
public interface CommandHandler {
    void handle(CommandContext context) throws CommandException;
}
