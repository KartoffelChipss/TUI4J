package org.strassburger.tui4j.command;

import java.util.Map;

/**
 * Represents the context of a command execution, including its name, arguments, and options.
 */
public class CommandContext {
    private final String commandName;
    private final Map<Argument<?>, Object> arguments;
    private final Map<Option<?>, Object> options;

    /**
     * Constructs a CommandContext with the given options map.
     * @param commandName The name of the command
     * @param options A map of Option instances to their corresponding values
     */
    public CommandContext(String commandName, Map<Argument<?>, Object> arguments, Map<Option<?>, Object> options) {
        this.commandName = commandName;
        this.arguments = arguments;
        this.options = options;

        for (var entry : options.entrySet()) {
            String optionsString = String.format("Option: %s, Value: %s", entry.getKey().getLongName(), entry.getValue());
            System.out.println(optionsString);
        }
    }

    /**
     * Retrieves the name of the command associated with this context.
     * @return The command name
     */
    public String getCommandName() {
        return commandName;
    }

    /**
     * Retrieves the value of the specified option.
     * If the option is not present in the context, returns its default value.
     * @param option The option whose value is to be retrieved
     * @param <T> The type of the option's value
     * @return The value of the option, or its default value if not set
     */
    public <T> T getOptionValue(Option<T> option) {
        if (!options.containsKey(option)) return option.getDefaultValue();
        return option.getType().cast(options.get(option));
    }

    /**
     * Retrieves the value of the specified option by its name.
     * @param optionName The name of the option (long or short)
     * @param type The class type of the option's value
     * @param <T> The type of the option's value
     * @return The value of the option, or null if not found
     */
    public <T> T getOptionValue(String optionName, Class<T> type) {
        for (var entry : options.entrySet()) {
            if (entry.getKey().getLongName().equals(optionName)
                    || entry.getKey().getShortName().equals(optionName)) {
                return type.cast(entry.getValue());
            }
        }
        return null;
    }

    /**
     * Retrieves the value of the specified argument.
     * @param argument The argument whose value is to be retrieved
     * @param <T> The type of the argument's value
     * @return The value of the argument
     */
    public <T> T getArgumentValue(Argument<T> argument) {
        if (!arguments.containsKey(argument)) return null;
        return argument.getType().cast(arguments.get(argument));
    }

    /**
     * Retrieves the value of the specified argument by its name.
     * @param argumentName The name of the argument
     * @param type The class type of the argument's value
     * @param <T> The type of the argument's value
     * @return The value of the argument, or null if not found
     */
    public <T> T getArgumentValue(String argumentName, Class<T> type) {
        for (var entry : arguments.entrySet()) {
            if (entry.getKey().getName().equals(argumentName)) {
                return type.cast(entry.getValue());
            }
        }
        return null;
    }

    /**
     * Retrieves the map of options and their corresponding values.
     * @return A map of Option instances to their values
     */
    public Map<Option<?>, Object> getOptions() {
        return options;
    }

    /**
     * Retrieves the map of arguments and their corresponding values.
     * @return A map of Argument instances to their values
     */
    public Map<Argument<?>, Object> getArguments() {
        return arguments;
    }
}
