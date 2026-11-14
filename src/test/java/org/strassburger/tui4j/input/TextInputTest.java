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

class TextInputTest {

    private TextInput textInput;
    private Console console;
    private ValidationRule<String> minFifeCharsRule;

    @BeforeEach
    void setUp() {
        console = mock(Console.class);
        textInput = new TextInput();
        minFifeCharsRule = new ValidationRule<>() {
            @Override
            public boolean validate(String input) {
                return input.length() > 5;
            }

            @Override
            public StyledText errorMessage() {
                return StyledText.text("Name must be longer than 5 characters.").fg(AnsiColor.RED);
            }
        };
    }

    @Test
    void testValidInput() throws InputValidationException {
        when(console.readLine()).thenReturn("John Doe");

        textInput.prompt("What is your name?");

        String result = textInput.read(console);

        assertEquals("John Doe", result);
        verify(console, times(1)).readLine();
    }

    @Test
    void testEmptyInput() throws InputValidationException {
        when(console.readLine()).thenReturn("").thenReturn("John Doe");

        textInput.prompt("What is your name?");

        String result = textInput.read(console);

        assertEquals("John Doe", result);
        verify(console, times(2)).readLine();
    }

    @Test
    void testInputWithValidationRule() {
        when(console.readLine()).thenReturn("John");

        textInput.prompt("What is your name?");
        textInput.retryOnInvalid(false);
        textInput.validationRule(minFifeCharsRule);

        InputValidationException exception = assertThrows(InputValidationException.class, () -> textInput.read(console));
        assertEquals("Name must be longer than 5 characters.", exception.getMessage());
        verify(console, times(1)).readLine();
    }

    @Test
    void testInputWithRetryOnInvalid() throws InputValidationException {
        when(console.readLine()).thenReturn("John", "John", "John Doe");

        textInput.prompt("What is your name?");
        textInput.retryOnInvalid(true);
        textInput.validationRule(minFifeCharsRule);

        String result = textInput.read(console);

        assertEquals("John Doe", result);
        verify(console, times(3)).readLine();
    }
}