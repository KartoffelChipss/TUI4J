package org.strassburger.input.validationrules;

public interface ValidationRule<T> {
    boolean validate(T input);
    String getErrorMessage();
}