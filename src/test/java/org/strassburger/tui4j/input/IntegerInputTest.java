package org.strassburger.tui4j.input;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.strassburger.tui4j.input.IntegerInput;
import org.strassburger.tui4j.input.exceptions.InputValidationException;
import org.strassburger.tui4j.input.validationrules.NumberValidationRules;
import org.strassburger.tui4j.console.Console;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class IntegerInputTest {

    private IntegerInput input;
    private Console console;

    @BeforeEach
    void setUp() {
        console = mock(Console.class);
        input = new IntegerInput();
    }

    @Test
    void testValidInput() {
        when(console.readLine()).thenReturn("100");

        input.setPrompt("What is your age?");

        int result = input.read(console);

        assertEquals(100, result);
        verify(console, times(1)).readLine();
    }

    @Test
    void testInvalidInput() {
        when(console.readLine()).thenReturn("abc");

        input.setRetryOnInvalid(false);
        input.setPrompt("What is your age?");

        assertThrows(InputValidationException.class, () -> input.read(console));
        verify(console, times(1)).readLine();
    }

    @Test
    void testRetryOnInvalid() {
        when(console.readLine()).thenReturn("abc").thenReturn("100");

        input.setRetryOnInvalid(true);
        input.setPrompt("What is your age?");

        int result = input.read(console);

        assertEquals(100, result);
        verify(console, times(2)).readLine();
    }

    @Test
    void testValidationRules() {
        when(console.readLine()).thenReturn("0").thenReturn("100");

        input.setPrompt("What is your age?");
        input.addValidationRule(NumberValidationRules.greaterThan(0));

        int result = input.read(console);

        assertEquals(100, result);
        verify(console, times(2)).readLine();
    }
}
