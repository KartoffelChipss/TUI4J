package org.strassburger.tui4j.command;

public class Option<T> {
    private final String longName;
    private final String shortName;
    private final Class<T> type;
    private final T defaultValue;

    public Option(String longName, String shortName, Class<T> type, T defaultValue) {
        this.longName = longName;
        this.shortName = shortName;
        this.type = type;
        this.defaultValue = defaultValue;
    }

    public String getLongName() {
        return longName;
    }

    public String getShortName() {
        return shortName;
    }

    public Class<T> getType() {
        return type;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public static <T> Option<T> of(String longName, String shortName, Class<T> type, T defaultValue) {
        return new Option<>(longName, shortName, type, defaultValue);
    }

    /**
     * Creates a boolean option.
     * @param longName The long name of the option (e.g., "--verbose")
     * @param shortName The short name of the option (e.g., "-v")
     * @return An Option instance for a boolean type
     */
    public static Option<Boolean> bool(String longName, String shortName) {
        return new Option<>(longName, shortName, Boolean.class, false);
    }

    /**
     * Creates a string option.
     * @param longName The long name of the option (e.g., "--output")
     * @param shortName The short name of the option (e.g., "-o")
     * @param defaultValue The default value of the option
     * @return An Option instance for a string type
     */
    public static Option<String> str(String longName, String shortName, String defaultValue) {
        return new Option<>(longName, shortName, String.class, defaultValue);
    }

    /**
     * Creates an integer option.
     * @param longName The long name of the option (e.g., "--count")
     * @param shortName The short name of the option (e.g., "-c")
     * @param defaultValue The default value of the option
     * @return An Option instance for an integer type
     */
    public static Option<Integer> integer(String longName, String shortName, Integer defaultValue) {
        return new Option<>(longName, shortName, Integer.class, defaultValue);
    }

    /**
     * Creates a double option.
     * @param longName The long name of the option (e.g., "--threshold")
     * @param shortName The short name of the option (e.g., "-t")
     * @param defaultValue The default value of the option
     * @return An Option instance for a double type
     */
    public static Option<Double> dbl(String longName, String shortName, Double defaultValue) {
        return new Option<>(longName, shortName, Double.class, defaultValue);
    }

    /**
     * Creates a float option.
     * @param longName The long name of the option (e.g., "--ratio")
     * @param shortName The short name of the option (e.g., "-r")
     * @param defaultValue The default value of the option
     * @return An Option instance for a float type
     */
    public static Option<Float> flt(String longName, String shortName, Float defaultValue) {
        return new Option<>(longName, shortName, Float.class, defaultValue);
    }
}
