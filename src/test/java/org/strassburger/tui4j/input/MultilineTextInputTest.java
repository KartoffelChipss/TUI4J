package org.strassburger.tui4j.input;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.strassburger.tui4j.formatting.StyledText;
import org.strassburger.tui4j.formatting.ansi.AnsiColor;
import org.strassburger.tui4j.input.MultilineTextInput;
import org.strassburger.tui4j.input.exceptions.InputValidationException;
import org.strassburger.tui4j.input.validationrules.ValidationRule;

import java.util.Scanner;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class MultilineTextInputTest {

    private MultilineTextInput multilineTextInput;
    private Scanner mockScanner;
    private ValidationRule<String> minThirtyharsRule;

    @BeforeEach
    void setUp() {
        mockScanner = mock(Scanner.class);
        multilineTextInput = new MultilineTextInput() {
            @Override
            protected Scanner getScanner() {
                return mockScanner;
            }
        };
        minThirtyharsRule = new ValidationRule<String>() {
            @Override
            public boolean validate(String input) {
                return input.length() >= 30;
            }

            @Override
            public StyledText getErrorMessage() {
                return StyledText.text("Name must be longer than 30 characters.").fg(AnsiColor.RED);
            }
        };
    }

    @Test
    void testValidInput() throws InputValidationException {
        when(mockScanner.nextLine()).thenReturn("Das ist die erste zeile", "und das die zweite", "und das die dritte", "");

        multilineTextInput.setLabel("Tell us about yourself");

        String result = multilineTextInput.read();

        assertEquals("Das ist die erste zeile\nund das die zweite\nund das die dritte", result);
        verify(mockScanner, times(4)).nextLine();
    }

    @Test
    void testInputWithValidationRule() {
        when(mockScanner.nextLine()).thenReturn("I", "am", "god", "");

        multilineTextInput.setLabel("Tell us about yourself");
        multilineTextInput.setRetryOnInvalid(false);
        multilineTextInput.addValidationRule(minThirtyharsRule);

        InputValidationException exception = assertThrows(InputValidationException.class, multilineTextInput::read);
        assertEquals("Name must be longer than 30 characters.", exception.getMessage());
        verify(mockScanner, times(4)).nextLine();
    }

    @Test
    void testInputWithRetryOnInvalid() throws InputValidationException {
        when(mockScanner.nextLine()).thenReturn("I", "am", "god", "", "Das ist die erste zeile", "und das die zweite", "und das die dritte", "");

        multilineTextInput.setLabel("Tell us about yourself");
        multilineTextInput.setRetryOnInvalid(true);
        multilineTextInput.addValidationRule(minThirtyharsRule);

        String result = multilineTextInput.read();

        assertEquals("Das ist die erste zeile\nund das die zweite\nund das die dritte", result);
        verify(mockScanner, times(8)).nextLine();
    }
}