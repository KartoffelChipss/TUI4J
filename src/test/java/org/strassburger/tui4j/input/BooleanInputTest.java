package org.strassburger.tui4j.input;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.strassburger.tui4j.input.BooleanInput;
import org.strassburger.tui4j.input.exceptions.InputValidationException;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class BooleanInputTest {
    private BooleanInput input;
    private Scanner mockScanner;

    @BeforeEach
    void setUp() {
        mockScanner = mock(Scanner.class);
        input = new BooleanInput() {
            @Override
            protected Scanner getScanner() {
                return mockScanner;
            }
        };
    }

    @Test
    void testValidInput() throws InputValidationException {
        when(mockScanner.nextLine()).thenReturn("yes");

        input.setLabel("Do you like ice cream?");

        boolean result = input.read();

        assertTrue(result);
        verify(mockScanner, times(1)).nextLine();
    }

    @Test
    void testInvalidInput() {
        when(mockScanner.nextLine()).thenReturn("maybe");

        input.setRetryOnInvalid(false);
        input.setLabel("Do you like ice cream?");

        assertThrows(InputValidationException.class, input::read);
        verify(mockScanner, times(1)).nextLine();
    }

    private void testBooleanInput(String inputValue, boolean expectedResult) throws InputValidationException {
        when(mockScanner.nextLine()).thenReturn(inputValue);
        input.setLabel("Do you like ice cream?");

        boolean result = input.read();

        assertEquals(expectedResult, result);
        verify(mockScanner, times(1)).nextLine();
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
