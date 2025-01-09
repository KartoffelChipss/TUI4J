package org.strassburger.tui4j.input;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.strassburger.tui4j.input.SelectInput;
import org.strassburger.tui4j.input.exceptions.InputValidationException;

import java.util.Scanner;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

public class SelectInputTest {
    private SelectInput input;
    private Scanner mockScanner;

    @BeforeEach
    void setUp() {
        mockScanner = mock(Scanner.class);
        input = new SelectInput() {
            @Override
            protected Scanner getScanner() {
                return mockScanner;
            }
        };
    }

    @Test
    void testValidInput() {
        when(mockScanner.nextLine()).thenReturn("1");

        input.addOptions("John Doe", "Jane Doe", "John Smith");
        input.setLabel("What is your name?");

        String result = input.read();

        assertEquals("John Doe", result);
        verify(mockScanner, times(1)).nextLine();
    }

    @Test
    void testInvalidInput() {
        when(mockScanner.nextLine()).thenReturn("4");

        input.addOptions("John Doe", "Jane Doe", "John Smith");
        input.setLabel("What is your name?");
        input.setRetryOnInvalid(false);

        assertThrows(InputValidationException.class, input::read);
        verify(mockScanner, times(1)).nextLine();
    }

    @Test
    void testRetryOnInvalid() {
        when(mockScanner.nextLine()).thenReturn("4").thenReturn("1");

        input.addOptions("John Doe", "Jane Doe", "John Smith");
        input.setLabel("What is your name?");
        input.setRetryOnInvalid(true);

        String result = input.read();

        assertEquals("John Doe", result);
        verify(mockScanner, times(2)).nextLine();
    }
}
