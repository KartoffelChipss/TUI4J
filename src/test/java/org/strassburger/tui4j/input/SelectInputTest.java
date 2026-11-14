package org.strassburger.tui4j.input;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.strassburger.tui4j.input.exceptions.InputValidationException;
import org.strassburger.tui4j.console.Console;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class SelectInputTest {

    private SelectInput<String> input;
    private Console console;

    @BeforeEach
    void setUp() {
        console = mock(Console.class);
        input = new SelectInput<>();
    }

    @Test
    void testValidInput() {
        when(console.readLine()).thenReturn("1");

        input.option("Option A", "A")
                .option("Option B", "B")
                .option("Option C", "C");
        input.prompt("Choose an option:");

        String result = input.read(console);

        assertEquals("A", result);
        verify(console, times(1)).readLine();
    }

    @Test
    void testInvalidInput() {
        when(console.readLine()).thenReturn("4");

        input.option("Option A", "A")
                .option("Option B", "B")
                .option("Option C", "C");
        input.prompt("Choose an option:");
        input.retryOnInvalid(false);

        assertThrows(InputValidationException.class, () -> input.read(console));
        verify(console, times(1)).readLine();
    }

    @Test
    void testRetryOnInvalidInput() {
        when(console.readLine()).thenReturn("4").thenReturn("1");

        input.option("Option A", "A")
                .option("Option B", "B")
                .option("Option C", "C");
        input.prompt("Choose an option:");
        input.retryOnInvalid(true);

        String result = input.read(console);

        assertEquals("A", result);
        verify(console, times(2)).readLine();
    }

    @Test
    void testEmptyInput() {
        when(console.readLine()).thenReturn("");

        input.option("Option A", "A")
                .option("Option B", "B")
                .option("Option C", "C");
        input.prompt("Choose an option:");
        input.retryOnInvalid(false);

        assertThrows(InputValidationException.class, () -> input.read(console));
        verify(console, times(1)).readLine();
    }

    @Test
    void testInvalidFormatInput() {
        when(console.readLine()).thenReturn("abc");

        input.option("Option A", "A")
                .option("Option B", "B")
                .option("Option C", "C");
        input.prompt("Choose an option:");
        input.retryOnInvalid(false);

        assertThrows(InputValidationException.class, () -> input.read(console));
        verify(console, times(1)).readLine();
    }

    @Test
    void testOptionsStyle() {
        when(console.readLine()).thenReturn("2");

        input.option("Option A", "A")
                .option("Option B", "B")
                .option("Option C", "C");
        input.prompt("Choose an option:");
        input.optionsStyle(" &8(%num%) &7%label%");

        String result = input.read(console);

        assertEquals("B", result);
        verify(console, times(1)).readLine();
    }

    @Test
    void testRetryWithMultipleInvalidInputs() {
        when(console.readLine())
                .thenReturn("invalid")
                .thenReturn("5")
                .thenReturn("1");

        input.option("Option A", "A")
                .option("Option B", "B")
                .option("Option C", "C");
        input.prompt("Choose an option:");
        input.retryOnInvalid(true);

        String result = input.read(console);

        assertEquals("A", result);
        verify(console, times(3)).readLine();
    }
}