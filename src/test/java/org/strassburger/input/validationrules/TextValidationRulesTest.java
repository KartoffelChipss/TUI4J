package org.strassburger.input.validationrules;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TextValidationRulesTest {

    @Test
    void testNoSpaces() {
        ValidationRule<String> rule = TextValidationRules.noSpaces();

        assertTrue(rule.validate("NoSpaces"));
        assertFalse(rule.validate("Spaces Here"));
        assertEquals("This field cannot contain spaces.", rule.getErrorMessage());
    }

    @Test
    void testMinLength() {
        ValidationRule<String> rule = TextValidationRules.minLength(5);

        assertTrue(rule.validate("Valid"));
        assertFalse(rule.validate("Too"));
        assertEquals("This field must be at least 5 characters long.", rule.getErrorMessage());
    }

    @Test
    void testMaxLength() {
        ValidationRule<String> rule = TextValidationRules.maxLength(5);

        assertTrue(rule.validate("Short"));
        assertFalse(rule.validate("ThisIsTooLong"));
        assertEquals("This field must be at most 5 characters long.", rule.getErrorMessage());
    }

    @Test
    void testDisallowEmpty() {
        ValidationRule<String> rule = TextValidationRules.disallowEmpty();

        assertTrue(rule.validate("NotEmpty"));
        assertFalse(rule.validate(""));
        assertEquals("This field cannot be empty.", rule.getErrorMessage());
    }

    @Test
    void testRegex() {
        ValidationRule<String> rule = TextValidationRules.regex("[A-Za-z]+");

        assertTrue(rule.validate("Valid"));
        assertFalse(rule.validate("Invalid123"));
        assertEquals("This field must match the following regex: [A-Za-z]+", rule.getErrorMessage());
    }

    @Test
    void testNoNumbers() {
        ValidationRule<String> rule = TextValidationRules.noNumbers();

        assertTrue(rule.validate("NoNumbers"));
        assertFalse(rule.validate("Has123Numbers"));
        assertEquals("This field cannot contain numbers.", rule.getErrorMessage());
    }

    @Test
    void testAlphanumeric() {
        ValidationRule<String> rule = TextValidationRules.alphanumeric();

        assertTrue(rule.validate("Valid123"));
        assertFalse(rule.validate("Invalid!@#"));
        assertEquals("This field must be alphanumeric.", rule.getErrorMessage());
    }
}