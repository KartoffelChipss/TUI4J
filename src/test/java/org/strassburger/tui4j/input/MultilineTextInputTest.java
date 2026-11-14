package org.strassburger.tui4j.input;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.strassburger.tui4j.formatting.StyledText;
import org.strassburger.tui4j.formatting.ansi.AnsiColor;
import org.strassburger.tui4j.input.exceptions.InputValidationException;
import org.strassburger.tui4j.input.validationrules.ValidationRule;
import org.strassburger.tui4j.console.Console;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class MultilineTextInputTest {

    private MultilineTextInput multilineTextInput;
    private Console console;
    private ValidationRule<String> minThirtyharsRule;

    @BeforeEach
    void setUp() {
        console = mock(Console.class);
        multilineTextInput = new MultilineTextInput();
        minThirtyharsRule = new ValidationRule<String>() {
            @Override
            public boolean validate(String input) {
                return input.length() >= 30;
            }

            @Override
            public StyledText errorMessage() {
                return StyledText.text("Name must be longer than 30 characters.").fg(AnsiColor.RED);
            }
        };
    }

    @Test
    void testValidInput() throws InputValidationException {
        when(console.readLine()).thenReturn("Das ist die erste zeile", "und das die zweite", "und das die dritte", "");

        multilineTextInput.prompt("Tell us about yourself");

        String result = multilineTextInput.read(console);

        assertEquals("Das ist die erste zeile\nund das die zweite\nund das die dritte", result);
        verify(console, times(4)).readLine();
    }

    @Test
    void testInputWithValidationRule() {
        when(console.readLine()).thenReturn("I", "am", "god", "");

        multilineTextInput.prompt("Tell us about yourself");
        multilineTextInput.retryOnInvalid(false);
        multilineTextInput.validationRule(minThirtyharsRule);

        InputValidationException exception = assertThrows(InputValidationException.class, () -> multilineTextInput.read(console));
        assertEquals("Name must be longer than 30 characters.", exception.getMessage());
        verify(console, times(4)).readLine();
    }

    @Test
    void testInputWithRetryOnInvalid() throws InputValidationException {
        when(console.readLine()).thenReturn("I", "am", "god", "", "Das ist die erste zeile", "und das die zweite", "und das die dritte", "");

        multilineTextInput.prompt("Tell us about yourself");
        multilineTextInput.retryOnInvalid(true);
        multilineTextInput.validationRule(minThirtyharsRule);

        String result = multilineTextInput.read(console);

        assertEquals("Das ist die erste zeile\nund das die zweite\nund das die dritte", result);
        verify(console, times(8)).readLine();
    }
}