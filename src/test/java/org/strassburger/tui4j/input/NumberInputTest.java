package org.strassburger.tui4j.input;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.strassburger.tui4j.input.exceptions.InputValidationException;
import org.strassburger.tui4j.console.Console;

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
    private Console console;

    @BeforeEach
    void setUp() {
        console = mock(Console.class);

        // Initialize all NumberInput types
        byteInput = new NumberInput<>(Byte.class);
        shortInput = new NumberInput<>(Short.class);
        integerInput = new NumberInput<>(Integer.class);
        longInput = new NumberInput<>(Long.class);
        floatInput = new NumberInput<>(Float.class);
        doubleInput = new NumberInput<>(Double.class);
    }

    // Helper method to execute the tests on different types
    private <T extends Number> void testValidInput(NumberInput<T> input, T expectedValue) {
        when(console.readLine()).thenReturn(expectedValue.toString());
        input.prompt("What is your value?");

        T result = input.read(console);

        assertEquals(expectedValue, result);
    }

    // Helper method to test invalid input behavior
    private <T extends Number> void testInvalidInput(NumberInput<T> input) {
        when(console.readLine()).thenReturn("abc");
        input.retryOnInvalid(false);
        input.prompt("What is your value?");

        assertThrows(InputValidationException.class, () -> input.read(console));
    }

    // Helper method to test retry behavior on invalid input
    private <T extends Number> void testRetryOnInvalid(NumberInput<T> input, T validValue) {
        when(console.readLine()).thenReturn("abc").thenReturn(validValue.toString());

        input.retryOnInvalid(true);
        input.prompt("What is your value?");

        T result = input.read(console);

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