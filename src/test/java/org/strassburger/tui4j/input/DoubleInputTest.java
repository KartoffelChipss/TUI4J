package org.strassburger.tui4j.input;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.strassburger.tui4j.input.DoubleInput;
import org.strassburger.tui4j.input.exceptions.InputValidationException;
import org.strassburger.tui4j.input.validationrules.NumberValidationRules;
import org.strassburger.tui4j.console.Console;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class DoubleInputTest {

    private DoubleInput input;
    private Console console;

    @BeforeEach
    void setUp() {
        console = mock(Console.class);
        input = new DoubleInput();
    }

    @Test
    void testValidInput() {
        when(console.readLine()).thenReturn("90.15");

        input.setPrompt("What is your weight?");

        double result = input.read(console);

        assertEquals(90.15, result);
        verify(console, times(1)).readLine();
    }

    @Test
    void testInvalidInput() {
        when(console.readLine()).thenReturn("abc");

        input.setRetryOnInvalid(false);
        input.setPrompt("What is your weight?");

        assertThrows(InputValidationException.class, () -> input.read(console));
        verify(console, times(1)).readLine();
    }

    @Test
    void testRetryOnInvalid() {
        when(console.readLine()).thenReturn("abc").thenReturn("90.15");

        input.setRetryOnInvalid(true);
        input.setPrompt("What is your weight?");

        double result = input.read(console);

        assertEquals(90.15, result);
        verify(console, times(2)).readLine();
    }

    @Test
    void testValidationRules() {
        when(console.readLine()).thenReturn("0.0");

        input.setPrompt("What is your weight?");
        input.setRetryOnInvalid(false);
        input.addValidationRule(NumberValidationRules.greaterThan(0.0));

        assertThrows(InputValidationException.class, () -> input.read(console));
        verify(console, times(1)).readLine();
    }

    @Test
    void testAllowComma() {
        when(console.readLine()).thenReturn("90,15");

        input.setPrompt("What is your weight?");
        input.setAllowComma(false);
        input.setRetryOnInvalid(false);

        assertThrows(InputValidationException.class, () -> input.read(console));
        verify(console, times(1)).readLine();
    }
}
