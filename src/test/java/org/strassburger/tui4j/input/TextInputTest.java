package org.strassburger.tui4j.input;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.strassburger.tui4j.formatting.StyledText;
import org.strassburger.tui4j.formatting.ansi.AnsiColor;
import org.strassburger.tui4j.input.TextInput;
import org.strassburger.tui4j.input.exceptions.InputValidationException;
import org.strassburger.tui4j.input.validationrules.ValidationRule;
import org.strassburger.tui4j.printer.TestPrinter;

import java.util.Scanner;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class TextInputTest {

    private final TestPrinter printer = new TestPrinter();

    private TextInput textInput;
    private Scanner mockScanner;
    private ValidationRule<String> minFifeCharsRule;

    @BeforeEach
    void setUp() {
        mockScanner = mock(Scanner.class);
        textInput = new TextInput(printer) {
            @Override
            protected Scanner getScanner() {
                return mockScanner;
            }
        };
        minFifeCharsRule = new ValidationRule<>() {
            @Override
            public boolean validate(String input) {
                return input.length() > 5;
            }

            @Override
            public StyledText getErrorMessage() {
                return StyledText.text("Name must be longer than 5 characters.").fg(AnsiColor.RED);
            }
        };
    }

    @Test
    void testValidInput() throws InputValidationException {
        when(mockScanner.nextLine()).thenReturn("John Doe");

        textInput.setPrompt("What is your name?");

        String result = textInput.read();

        assertEquals("John Doe", result);
        verify(mockScanner, times(1)).nextLine();
    }

    @Test
    void testEmptyInput() throws InputValidationException {
        when(mockScanner.nextLine()).thenReturn("").thenReturn("John Doe");

        textInput.setPrompt("What is your name?");

        String result = textInput.read();

        assertEquals("John Doe", result);
        verify(mockScanner, times(2)).nextLine();
    }

    @Test
    void testInputWithValidationRule() {
        when(mockScanner.nextLine()).thenReturn("John");

        textInput.setPrompt("What is your name?");
        textInput.setRetryOnInvalid(false);
        textInput.addValidationRule(minFifeCharsRule);

        InputValidationException exception = assertThrows(InputValidationException.class, textInput::read);
        assertEquals("Name must be longer than 5 characters.", exception.getMessage());
        verify(mockScanner, times(1)).nextLine();
    }

    @Test
    void testInputWithRetryOnInvalid() throws InputValidationException {
        when(mockScanner.nextLine()).thenReturn("John", "John", "John Doe");

        textInput.setPrompt("What is your name?");
        textInput.setRetryOnInvalid(true);
        textInput.addValidationRule(minFifeCharsRule);

        String result = textInput.read();

        assertEquals("John Doe", result);
        verify(mockScanner, times(3)).nextLine();
    }
}