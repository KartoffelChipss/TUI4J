package org.strassburger.tui4j.input;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.strassburger.tui4j.input.exceptions.InputValidationException;
import org.strassburger.tui4j.console.Console;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class BooleanInputTest {

    private BooleanInput input;
    private Console console;

    @BeforeEach
    void setUp() {
        console = mock(Console.class);
        input = new BooleanInput();
    }

    @Test
    void testValidInput() throws InputValidationException {
        when(console.readLine()).thenReturn("yes");

        input.prompt("Do you like ice cream?");

        boolean result = input.read(console);

        assertTrue(result);
        verify(console, times(1)).readLine();
    }

    @Test
    void testInvalidInput() {
        when(console.readLine()).thenReturn("maybe");

        input.retryOnInvalid(false);
        input.prompt("Do you like ice cream?");

        assertThrows(InputValidationException.class, () -> input.read(console));
        verify(console, times(1)).readLine();
    }

    private void testBooleanInput(String inputValue, boolean expectedResult) throws InputValidationException {
        when(console.readLine()).thenReturn(inputValue);
        input.prompt("Do you like ice cream?");

        boolean result = input.read(console);

        assertEquals(expectedResult, result);
        verify(console, times(1)).readLine();
    }

    @ParameterizedTest
    @CsvSource({
            "y, true",
            "yes, true",
            "n, false",
            "no, false"
    })
    void testValidInputs(String inputValue, boolean expectedResult) throws InputValidationException {
        testBooleanInput(inputValue, expectedResult);
    }
}
