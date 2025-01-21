package org.strassburger.tui4j.input;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.strassburger.tui4j.input.exceptions.InputValidationException;

import java.util.List;
import java.util.Scanner;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class SelectInputTest {
    private SelectInput<String> input;
    private Scanner mockScanner;

    @BeforeEach
    void setUp() {
        mockScanner = mock(Scanner.class);
        input = new SelectInput<>() {
            @Override
            protected Scanner getScanner() {
                return mockScanner;
            }
        };
    }

    @Test
    void testValidInput() {
        when(mockScanner.nextLine()).thenReturn("1");

        input.addOption("Option A", "A")
                .addOption("Option B", "B")
                .addOption("Option C", "C");
        input.setLabel("Choose an option:");

        String result = input.read();

        assertEquals("A", result);
        verify(mockScanner, times(1)).nextLine();
    }

    @Test
    void testInvalidInput() {
        when(mockScanner.nextLine()).thenReturn("4");

        input.addOption("Option A", "A")
                .addOption("Option B", "B")
                .addOption("Option C", "C");
        input.setLabel("Choose an option:");
        input.setRetryOnInvalid(false);

        assertThrows(InputValidationException.class, input::read);
        verify(mockScanner, times(1)).nextLine();
    }

    @Test
    void testRetryOnInvalidInput() {
        when(mockScanner.nextLine()).thenReturn("4").thenReturn("1");

        input.addOption("Option A", "A")
                .addOption("Option B", "B")
                .addOption("Option C", "C");
        input.setLabel("Choose an option:");
        input.setRetryOnInvalid(true);

        String result = input.read();

        assertEquals("A", result);
        verify(mockScanner, times(2)).nextLine();
    }

    @Test
    void testEmptyInput() {
        when(mockScanner.nextLine()).thenReturn("");

        input.addOption("Option A", "A")
                .addOption("Option B", "B")
                .addOption("Option C", "C");
        input.setLabel("Choose an option:");
        input.setRetryOnInvalid(false);

        assertThrows(InputValidationException.class, input::read);
        verify(mockScanner, times(1)).nextLine();
    }

    @Test
    void testInvalidFormatInput() {
        when(mockScanner.nextLine()).thenReturn("abc");

        input.addOption("Option A", "A")
                .addOption("Option B", "B")
                .addOption("Option C", "C");
        input.setLabel("Choose an option:");
        input.setRetryOnInvalid(false);

        assertThrows(InputValidationException.class, input::read);
        verify(mockScanner, times(1)).nextLine();
    }

    @Test
    void testOptionsStyle() {
        when(mockScanner.nextLine()).thenReturn("2");

        input.addOption("Option A", "A")
                .addOption("Option B", "B")
                .addOption("Option C", "C");
        input.setLabel("Choose an option:");
        input.setOptionsStyle(SelectInput.OptionsStyle.BRACKETS);

        String result = input.read();

        assertEquals("B", result);
        verify(mockScanner, times(1)).nextLine();
    }

    @Test
    void testRetryWithMultipleInvalidInputs() {
        when(mockScanner.nextLine())
                .thenReturn("invalid")
                .thenReturn("5")
                .thenReturn("1");

        input.addOption("Option A", "A")
                .addOption("Option B", "B")
                .addOption("Option C", "C");
        input.setLabel("Choose an option:");
        input.setRetryOnInvalid(true);

        String result = input.read();

        assertEquals("A", result);
        verify(mockScanner, times(3)).nextLine();
    }
}