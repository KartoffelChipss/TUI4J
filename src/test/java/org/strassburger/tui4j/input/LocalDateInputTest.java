package org.strassburger.tui4j.input;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.strassburger.tui4j.formatting.StyledText;
import org.strassburger.tui4j.formatting.ansi.AnsiColor;
import org.strassburger.tui4j.input.exceptions.InputValidationException;
import org.strassburger.tui4j.input.validationrules.ValidationRule;
import org.strassburger.tui4j.printer.TestPrinter;

import java.time.LocalDate;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class LocalDateInputTest {

    private final TestPrinter printer = new TestPrinter();

    private LocalDateInput dateInput;
    private Scanner mockScanner;

    @BeforeEach
    void setUp() {
        mockScanner = mock(Scanner.class);
        dateInput = new LocalDateInput(printer) {
            @Override
            protected Scanner getScanner() {
                return mockScanner;
            }
        };
    }

    @Test
    void testValidDateInput() throws InputValidationException {
        when(mockScanner.nextLine()).thenReturn("2023-12-31");

        dateInput.setPrompt("Enter a date (yyyy-MM-dd):");

        LocalDate result = dateInput.read();

        assertNotNull(result);
        verify(mockScanner, times(1)).nextLine();
        assertEquals("2023-12-31", result.toString());
    }

    @Test
    void testInvalidDateFormat() {
        when(mockScanner.nextLine()).thenReturn("31-12-2023");

        dateInput.setPrompt("Enter a date (yyyy-MM-dd):");
        dateInput.setRetryOnInvalid(false);

        InputValidationException exception = assertThrows(InputValidationException.class, dateInput::read);
        assertEquals("Invalid date format. Expected format: yyyy-MM-dd", exception.getMessage());
        verify(mockScanner, times(1)).nextLine();
    }

    @Test
    void testDateInputWithRetryOnInvalid() throws InputValidationException {
        when(mockScanner.nextLine()).thenReturn("31-12-2023", "2023-12-31");

        dateInput.setPrompt("Enter a date (yyyy-MM-dd):");
        dateInput.setRetryOnInvalid(true);

        LocalDate result = dateInput.read();

        assertNotNull(result);
        verify(mockScanner, times(2)).nextLine();
    }

    @Test
    void testDateInputWithCustomFormat() throws InputValidationException {
        when(mockScanner.nextLine()).thenReturn("31/12/2023");

        dateInput.setPrompt("Enter a date (dd/MM/yyyy):");
        dateInput.setDateFormat("dd/MM/yyyy");

        LocalDate result = dateInput.read();

        assertNotNull(result);
        verify(mockScanner, times(1)).nextLine();
    }

    @Test
    void testDateInputWithValidationRule() {
        when(mockScanner.nextLine()).thenReturn("2023-01-01");

        dateInput.setPrompt("Enter a date (yyyy-MM-dd):");
        dateInput.setRetryOnInvalid(false);
        dateInput.addValidationRule(new ValidationRule<>() {
            @Override
            public boolean validate(LocalDate input) {
                // Example validation: date must be after 2023-06-01
                return input.isAfter(LocalDate.of(2023, 6, 1)); // 2023-06-01
            }

            @Override
            public StyledText getErrorMessage() {
                return StyledText.text("Date must be after 2023-06-01.").fg(AnsiColor.RED);
            }
        });

        InputValidationException exception = assertThrows(InputValidationException.class, dateInput::read);
        assertEquals("Date must be after 2023-06-01.", exception.getMessage());
        verify(mockScanner, times(1)).nextLine();
    }
}
