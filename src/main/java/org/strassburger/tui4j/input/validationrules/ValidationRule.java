package org.strassburger.tui4j.input.validationrules;

public interface ValidationRule<T> {
    boolean validate(T input);
    String getErrorMessage();
}