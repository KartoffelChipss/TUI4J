package org.strassburger.tui4j.input;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.strassburger.tui4j.input.DoubleInput;
import org.strassburger.tui4j.input.exceptions.InputValidationException;
import org.strassburger.tui4j.input.validationrules.NumberValidationRules;
import org.strassburger.tui4j.printer.TestPrinter;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class DoubleInputTest {

    private final TestPrinter printer = new TestPrinter();

    private DoubleInput input;
    private Scanner mockScanner;

    @BeforeEach
    void setUp() {
        mockScanner = mock(Scanner.class);
        input = new DoubleInput(printer) {
            @Override
            protected Scanner getScanner() {
                return mockScanner;
            }
        };
    }

    @Test
    void testValidInput() {
        when(mockScanner.nextLine()).thenReturn("90.15");

        input.setPrompt("What is your weight?");

        double result = input.read();

        assertEquals(90.15, result);
        verify(mockScanner, times(1)).nextLine();
    }

    @Test
    void testInvalidInput() {
        when(mockScanner.nextLine()).thenReturn("abc");

        input.setRetryOnInvalid(false);
        input.setPrompt("What is your weight?");

        assertThrows(InputValidationException.class, input::read);
        verify(mockScanner, times(1)).nextLine();
    }

    @Test
    void testRetryOnInvalid() {
        when(mockScanner.nextLine()).thenReturn("abc").thenReturn("90.15");

        input.setRetryOnInvalid(true);
        input.setPrompt("What is your weight?");

        double result = input.read();

        assertEquals(90.15, result);
        verify(mockScanner, times(2)).nextLine();
    }

    @Test
    void testValidationRules() {
        when(mockScanner.nextLine()).thenReturn("0.0");

        input.setPrompt("What is your weight?");
        input.setRetryOnInvalid(false);
        input.addValidationRule(NumberValidationRules.greaterThan(0.0));

        assertThrows(InputValidationException.class, () -> input.read());
        verify(mockScanner, times(1)).nextLine();
    }

    @Test
    void testAllowComma() {
        when(mockScanner.nextLine()).thenReturn("90,15");

        input.setPrompt("What is your weight?");
        input.setAllowComma(false);
        input.setRetryOnInvalid(false);

        assertThrows(InputValidationException.class, () -> input.read());
        verify(mockScanner, times(1)).nextLine();
    }
}
