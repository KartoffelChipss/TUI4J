package org.strassburger.tui4j.command;

public class Argument<T> {
    private final String name;
    private final Class<T> type;
    private final boolean required;

    public Argument(String name, Class<T> type, boolean required) {
        this.name = name;
        this.type = type;
        this.required = required;
    }

    public String getName() {
        return name;
    }

    public Class<T> getType() {
        return type;
    }

    public boolean isRequired() {
        return required;
    }

    public static <T> Argument<T> of(String name, Class<T> type, boolean required) {
        return new Argument<>(name, type, required);
    }

    /**
     * Creates a string argument.
     * @param name The name of the argument
     * @param required Whether the argument is required
     * @return An Argument instance for a string type
     */
    public static Argument<String> str(String name, boolean required) {
        return new Argument<>(name, String.class, required);
    }

    /**
     * Creates an integer argument.
     * @param name The name of the argument
     * @param required Whether the argument is required
     * @return An Argument instance for an integer type
     */
    public static Argument<Integer> integer(String name, boolean required) {
        return new Argument<>(name, Integer.class, required);
    }

    /**
     * Creates a boolean argument.
     * @param name The name of the argument
     * @param required Whether the argument is required
     * @return An Argument instance for a boolean type
     */
    public static Argument<Boolean> bool(String name, boolean required) {
        return new Argument<>(name, Boolean.class, required);
    }

    /**
     * Creates a double argument.
     * @param name The name of the argument
     * @param required Whether the argument is required
     * @return An Argument instance for a double type
     */
    public static Argument<Double> dbl(String name, boolean required) {
        return new Argument<>(name, Double.class, required);
    }

    /**
     * Creates a float argument.
     * @param name The name of the argument
     * @param required Whether the argument is required
     * @return An Argument instance for a float type
     */
    public static Argument<Float> flt(String name, boolean required) {
        return new Argument<>(name, Float.class, required);
    }
}
