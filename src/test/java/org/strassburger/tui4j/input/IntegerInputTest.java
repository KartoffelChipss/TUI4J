package org.strassburger.tui4j.input;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.strassburger.tui4j.input.IntegerInput;
import org.strassburger.tui4j.input.exceptions.InputValidationException;
import org.strassburger.tui4j.input.validationrules.NumberValidationRules;
import org.strassburger.tui4j.printer.TestPrinter;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class IntegerInputTest {

    private final TestPrinter printer = new TestPrinter();

    private IntegerInput input;
    private Scanner mockScanner;

    @BeforeEach
    void setUp() {
        mockScanner = mock(Scanner.class);
        input = new IntegerInput(printer) {
            @Override
            protected Scanner getScanner() {
                return mockScanner;
            }
        };
    }

    @Test
    void testValidInput() {
        when(mockScanner.nextLine()).thenReturn("100");

        input.setPrompt("What is your age?");

        int result = input.read();

        assertEquals(100, result);
        verify(mockScanner, times(1)).nextLine();
    }

    @Test
    void testInvalidInput() {
        when(mockScanner.nextLine()).thenReturn("abc");

        input.setRetryOnInvalid(false);
        input.setPrompt("What is your age?");

        assertThrows(InputValidationException.class, input::read);
        verify(mockScanner, times(1)).nextLine();
    }

    @Test
    void testRetryOnInvalid() {
        when(mockScanner.nextLine()).thenReturn("abc").thenReturn("100");

        input.setRetryOnInvalid(true);
        input.setPrompt("What is your age?");

        int result = input.read();

        assertEquals(100, result);
        verify(mockScanner, times(2)).nextLine();
    }

    @Test
    void testValidationRules() {
        when(mockScanner.nextLine()).thenReturn("0").thenReturn("100");

        input.setPrompt("What is your age?");
        input.addValidationRule(NumberValidationRules.greaterThan(0));

        int result = input.read();

        assertEquals(100, result);
        verify(mockScanner, times(2)).nextLine();
    }
}
