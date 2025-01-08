package org.strassburger.input;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.strassburger.input.exceptions.InputValidationException;
import org.strassburger.input.validationrules.NumberValidationRules;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class DoubleInputTest {
    private DoubleInput input;
    private Scanner mockScanner;

    @BeforeEach
    void setUp() {
        mockScanner = mock(Scanner.class);
        input = new DoubleInput() {
            @Override
            protected Scanner getScanner() {
                return mockScanner;
            }
        };
    }

    @Test
    void testValidInput() {
        when(mockScanner.nextLine()).thenReturn("90.15");

        input.setLabel("What is your weight?");

        double result = input.read();

        assertEquals(90.15, result);
        verify(mockScanner, times(1)).nextLine();
    }

    @Test
    void testInvalidInput() {
        when(mockScanner.nextLine()).thenReturn("abc");

        input.setRetryOnInvalid(false);
        input.setLabel("What is your weight?");

        assertThrows(InputValidationException.class, input::read);
        verify(mockScanner, times(1)).nextLine();
    }

    @Test
    void testRetryOnInvalid() {
        when(mockScanner.nextLine()).thenReturn("abc").thenReturn("90.15");

        input.setRetryOnInvalid(true);
        input.setLabel("What is your weight?");

        double result = input.read();

        assertEquals(90.15, result);
        verify(mockScanner, times(2)).nextLine();
    }

    @Test
    void testValidationRules() {
        when(mockScanner.nextLine()).thenReturn("0.0");

        input.setLabel("What is your weight?");
        input.setRetryOnInvalid(false);
        input.addValidationRules(
                NumberValidationRules.greaterThan(0.0)
        );

        assertThrows(InputValidationException.class, () -> input.read());
        verify(mockScanner, times(1)).nextLine();
    }
}
