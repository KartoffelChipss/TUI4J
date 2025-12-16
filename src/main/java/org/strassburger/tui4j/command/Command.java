package org.strassburger.tui4j.command;

import org.strassburger.tui4j.command.exceptions.CommandException;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a command in a command-line interface (CLI) application.
 * A command can have sub-commands, options, arguments, and a handler to execute when the command is invoked.
 */
public class Command {
    private final String name;
    private final List<Command> subCommands;
    private final List<Option<?>> options;
    private final List<Argument<?>> arguments;
    private CommandHandler handler;

    public Command(String name, List<Command> subCommands, List<Option<?>> options, List<Argument<?>> arguments, CommandHandler handler) {
        this.name = name;
        this.subCommands = subCommands;
        this.options = options;
        this.arguments = arguments;
        this.handler = handler;
    }

    public String getName() {
        return name;
    }

    public List<Command> getSubCommands() {
        return subCommands;
    }

    /**
     * Adds one or more sub-commands to this command.<br/>
     * Example:
     * <pre>git commit</pre>
     * can be represented as:<br/>
     * <pre>
     *     Command git = Command.named("git")
     *         .addSubCommand(Command.named("commit"));
     * </pre>
     * @param command The Command(s) to add as sub-commands
     * @return The current Command instance for chaining
     */
    public Command addSubCommand(Command... command) {
        this.subCommands.addAll(List.of(command));
        return this;
    }

    public List<Option<?>> getOptions() {
        return options;
    }

    /**
     * Adds one or more options to this command. Options are named parameters that the command can accept.<br/>
     * Example:
     * <pre>remove --force --file myfile</pre>
     * can be represented as:<br/>
     * <pre>
     *     Command cmd = Command.named("remove")
     *         .addOption(Option.bool("force", "force"), Option.str("file", "f", null));
     * </pre>
     * @param option The Option(s) to add
     * @return The current Command instance for chaining
     */
    public Command addOption(Option<?>... option) {
        this.options.addAll(List.of(option));
        return this;
    }

    public List<Argument<?>> getArguments() {
        return arguments;
    }

    /**
     * Adds one or more arguments to this command. Arguments are positional parameters that the command can accept.<br/>
     * Example:
     * <pre>copy source destination</pre>
     * can be represented as:<br/>
     * <pre>
     *     Command cmd = Command.named("copy")
     *         .addArgument(Argument.str("source", true), Argument.str("destination", true));
     * </pre>
     * @param argument The Argument(s) to add
     * @return The current Command instance for chaining
     */
    public Command addArgument(Argument<?>... argument) {
        this.arguments.addAll(List.of(argument));
        return this;
    }

    public CommandHandler getHandler() {
        return handler;
    }

    /**
     * Sets the handler for this command.
     * @param handler The CommandHandler to set
     * @return The current Command instance for chaining
     */
    public Command setHandler(CommandHandler handler) {
        this.handler = handler;
        return this;
    }

    /**
     * Creates a named command with the given name. This command can have sub-commands, options, and arguments.
     * @param name The name of the command
     * @return A new Command instance with the specified name
     */
    public static Command named(String name) {
        return new Command(name, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), null);
    }

    /**
     * Creates a root command with no name. This command can have sub-commands, options, and arguments.
     * @return A new root Command instance
     */
    public static Command root() {
        return new Command(null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), null);
    }

    private String getFirstNotOptionArg(String[] args) {
        for (String arg : args) {
            if (!arg.startsWith("-")) {
                return arg;
            }
        }
        return null;
    }

    /**
     * Parses the given arguments and executes the command's handler.
     * If a sub-command is detected, it delegates parsing and execution to the sub-command.
     *
     * @param args The command-line arguments to parse
     * @throws CommandException if an error occurs during parsing or execution
     */
    public void execute(String[] args) throws CommandException {
        if (handler == null && name != null) throw new IllegalStateException("No handler defined for command: " + name);

        String firstArg = getFirstNotOptionArg(args);
        if (firstArg != null) {
            for (Command subCommand : subCommands) {
                if (subCommand.getName().equals(firstArg)) {
                    String[] subArgs = new String[args.length - 1];
                    int index = 0;
                    boolean skipped = false;
                    for (String arg : args) {
                        if (!skipped && arg.equals(firstArg)) {
                            skipped = true;
                            continue;
                        }
                        subArgs[index++] = arg;
                    }
                    subCommand.execute(subArgs);
                    return;
                }
            }
        }

        if (handler == null) throw new IllegalStateException("No handler defined for root command");

        handler.handle(CommandParser.parse(this, args));
    }
}
