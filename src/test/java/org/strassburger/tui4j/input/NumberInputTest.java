package org.strassburger.tui4j.input;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.strassburger.tui4j.input.NumberInput;
import org.strassburger.tui4j.input.exceptions.InputValidationException;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class NumberInputTest {
    private NumberInput<Byte> byteInput;
    private NumberInput<Short> shortInput;
    private NumberInput<Integer> integerInput;
    private NumberInput<Long> longInput;
    private NumberInput<Float> floatInput;
    private NumberInput<Double> doubleInput;
    private Scanner mockScanner;

    @BeforeEach
    void setUp() {
        mockScanner = mock(Scanner.class);

        // Initialize all NumberInput types
        byteInput = new NumberInput<>(Byte.class) {
            @Override
            protected Scanner getScanner() {
                return mockScanner;
            }
        };
        shortInput = new NumberInput<>(Short.class) {
            @Override
            protected Scanner getScanner() {
                return mockScanner;
            }
        };
        integerInput = new NumberInput<>(Integer.class) {
            @Override
            protected Scanner getScanner() {
                return mockScanner;
            }
        };
        longInput = new NumberInput<>(Long.class) {
            @Override
            protected Scanner getScanner() {
                return mockScanner;
            }
        };
        floatInput = new NumberInput<>(Float.class) {
            @Override
            protected Scanner getScanner() {
                return mockScanner;
            }
        };
        doubleInput = new NumberInput<>(Double.class) {
            @Override
            protected Scanner getScanner() {
                return mockScanner;
            }
        };
    }

    // Helper method to run the tests on different types
    private <T extends Number> void testValidInput(NumberInput<T> input, T expectedValue) {
        when(mockScanner.nextLine()).thenReturn(expectedValue.toString());
        input.setLabel("What is your value?");

        T result = input.read();

        assertEquals(expectedValue, result);
    }

    // Helper method to test invalid input behavior
    private <T extends Number> void testInvalidInput(NumberInput<T> input) {
        when(mockScanner.nextLine()).thenReturn("abc");
        input.setRetryOnInvalid(false);
        input.setLabel("What is your value?");

        assertThrows(InputValidationException.class, input::read);
    }

    // Helper method to test retry behavior on invalid input
    private <T extends Number> void testRetryOnInvalid(NumberInput<T> input, T validValue) {
        when(mockScanner.nextLine()).thenReturn("abc").thenReturn(validValue.toString());

        input.setRetryOnInvalid(true);
        input.setLabel("What is your value?");

        T result = input.read();

        assertEquals(validValue, result);
    }

    @Test
    void testValidInput() {
        testValidInput(byteInput, (byte) 100);
        testValidInput(shortInput, (short) 100);
        testValidInput(integerInput, 100);
        testValidInput(longInput, 100L);
        testValidInput(floatInput, 100.0f);
        testValidInput(doubleInput, 100.0);
    }

    @Test
    void testInvalidInput() {
        testInvalidInput(byteInput);
        testInvalidInput(shortInput);
        testInvalidInput(integerInput);
        testInvalidInput(longInput);
        testInvalidInput(floatInput);
        testInvalidInput(doubleInput);
    }

    @Test
    void testRetryOnInvalid() {
        testRetryOnInvalid(byteInput, (byte) 100);
        testRetryOnInvalid(shortInput, (short) 100);
        testRetryOnInvalid(integerInput, 100);
        testRetryOnInvalid(longInput, 100L);
        testRetryOnInvalid(floatInput, 100.0f);
        testRetryOnInvalid(doubleInput, 100.0);
    }
}