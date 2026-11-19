package org.strassburger.tui4j.input;

import org.junit.jupiter.api.Test;
import org.strassburger.tui4j.console.Console;
import org.strassburger.tui4j.input.exceptions.InputValidationException;
import org.strassburger.tui4j.input.validationrules.TextValidationRules;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PasswordInputTest {

    private final Console console = mock(Console.class);

    @Test
    void readsWithDefaultMask() throws InputValidationException {
        when(console.readPassword('*')).thenReturn("hunter2");

        assertEquals("hunter2", PasswordInput.withPrompt("Password").read(console));
        verify(console, never()).readLine();
    }

    @Test
    void readsWithCustomMask() throws InputValidationException {
        when(console.readPassword('•')).thenReturn("hunter2");

        PasswordInput input = PasswordInput.withPrompt("Password").mask('•');

        assertEquals('•', input.mask());
        assertEquals("hunter2", input.read(console));
    }

    @Test
    void hiddenReadsWithoutMask() throws InputValidationException {
        when(console.readPassword(null)).thenReturn("1234");

        PasswordInput input = PasswordInput.withPrompt("PIN").hidden();

        assertNull(input.mask());
        assertEquals("1234", input.read(console));
    }

    @Test
    void asksAgainOnEmptyInput() throws InputValidationException {
        when(console.readPassword(any())).thenReturn("").thenReturn("hunter2");

        assertEquals("hunter2", new PasswordInput().read(console));
        verify(console, times(2)).readPassword('*');
    }

    @Test
    void returnsDefaultValueOnEmptyInput() throws InputValidationException {
        when(console.readPassword(any())).thenReturn("");

        assertEquals("default", new PasswordInput().defaultValue("default").read(console));
    }

    @Test
    void retriesWhenValidationFails() throws InputValidationException {
        when(console.readPassword(any())).thenReturn("short").thenReturn("long enough");

        String result = new PasswordInput()
                .validationRule(TextValidationRules.minLength(8))
                .read(console);

        assertEquals("long enough", result);
        verify(console, times(2)).readPassword('*');
    }

    @Test
    void consoleDefaultFallsBackToReadLine() {
        Console plain = mock(Console.class, CALLS_REAL_METHODS);
        doReturn("visible").when(plain).readLine();

        assertEquals("visible", plain.readPassword('*'));
    }
}
