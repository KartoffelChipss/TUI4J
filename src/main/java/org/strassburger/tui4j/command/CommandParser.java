package org.strassburger.tui4j.command;

import java.util.HashMap;
import java.util.Map;
import org.strassburger.tui4j.command.exceptions.*;

public class CommandParser {
    private CommandParser() {}

    public static CommandContext parse(Command command, String[] args) throws CommandException {
        Map<Argument<?>, Object> arguments = new HashMap<>();
        Map<Option<?>, Object> options = new HashMap<>();

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.startsWith("--")) {
                i = parseLongOption(command, args, i, options);
            } else if (arg.startsWith("-")) {
                i = parseShortOption(command, args, i, options);
            } else {
                i = parseArgument(command, arguments, args, i);
            }
        }

        for (Argument<?> argument : command.getArguments()) {
            if (!arguments.containsKey(argument) && argument.isRequired()) {
                throw new MissingArgumentException(argument.getName());
            }
        }

        return new CommandContext(command.getName(), arguments, options);
    }

    private static int parseLongOption(
            Command command,
            String[] args,
            int index,
            Map<Option<?>, Object> options
    ) throws CommandException {
        String optionName = args[index].substring(2);

        Option<?> option = command.getOptions().stream()
                .filter(opt -> opt.getLongName().equals(optionName))
                .findFirst()
                .orElseThrow(() -> new UnknownOptionException(optionName));

        Object value;
        if (option.getType() == Boolean.class) {
            value = true;
        } else {
            if (index + 1 >= args.length) {
                throw new MissingOptionValueException(optionName);
            }
            value = parseValue(option.getType(), args[++index]);
        }

        options.put(option, value);
        return index;
    }

    private static int parseShortOption(
            Command command,
            String[] args,
            int index,
            Map<Option<?>, Object> options
    ) throws CommandException {
        String optionName = args[index].substring(1);

        Option<?> option = command.getOptions().stream()
                .filter(opt -> opt.getShortName().equals(optionName))
                .findFirst()
                .orElseThrow(() -> new UnknownOptionException(optionName));

        Object value;
        if (option.getType() == Boolean.class) {
            value = true;
        } else {
            if (index + 1 >= args.length) {
                throw new MissingOptionValueException(optionName);
            }
            value = parseValue(option.getType(), args[++index]);
        }

        options.put(option, value);
        return index;
    }

    private static int parseArgument(
            Command command,
            Map<Argument<?>, Object> arguments,
            String[] args,
            int index
    ) throws CommandException {
        int argIndex = arguments.size();
        if (argIndex >= command.getArguments().size()) {
            throw new TooManyArgumentsException();
        }

        Argument<?> argument = command.getArguments().get(argIndex);
        Object value = parseValue(argument.getType(), args[index]);
        arguments.put(argument, value);

        return index;
    }

    private static Object parseValue(Class<?> type, String valueStr) throws CommandException {
        try {
            if (type == Integer.class) {
                return Integer.parseInt(valueStr);
            } else if (type == Float.class) {
                return Float.parseFloat(valueStr);
            } else if (type == Double.class) {
                return Double.parseDouble(valueStr);
            } else if (type == String.class) {
                return valueStr;
            } else if (type == Boolean.class) {
                return Boolean.parseBoolean(valueStr);
            }
        } catch (NumberFormatException e) {
            throw new InvalidValueException(valueStr, type, e);
        }

        throw new UnsupportedTypeException(type);
    }
}