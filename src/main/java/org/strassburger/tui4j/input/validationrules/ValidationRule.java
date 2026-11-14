package org.strassburger.tui4j.input.validationrules;

import org.jspecify.annotations.Nullable;
import org.strassburger.tui4j.formatting.StyledText;

public interface ValidationRule<T> {
    boolean validate(T input);
    @Nullable StyledText errorMessage();
}