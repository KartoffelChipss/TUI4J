package org.strassburger.tui4j.input.validationrules;

import org.junit.jupiter.api.Test;
import org.strassburger.tui4j.input.validationrules.NumberValidationRules;
import org.strassburger.tui4j.input.validationrules.ValidationRule;

import static org.junit.jupiter.api.Assertions.*;

class NumberValidationRulesTest {

    @Test
    void testLessThan() {
        ValidationRule<Integer> rule = NumberValidationRules.lessThan(10);

        assertTrue(rule.validate(5));
        assertFalse(rule.validate(15));

        assertEquals("This field must be less than 10.", rule.getErrorMessage());
    }

    @Test
    void testGreaterThan() {
        ValidationRule<Integer> rule = NumberValidationRules.greaterThan(10);

        assertTrue(rule.validate(15));
        assertFalse(rule.validate(5));

        assertEquals("This field must be greater than 10.", rule.getErrorMessage());
    }

    @Test
    void testLessThanOrEqualTo() {
        ValidationRule<Integer> rule = NumberValidationRules.lessThanOrEqualTo(10);

        assertTrue(rule.validate(5));
        assertTrue(rule.validate(10));
        assertFalse(rule.validate(15));

        assertEquals("This field must be less than or equal to 10.", rule.getErrorMessage());
    }

    @Test
    void testGreaterThanOrEqualTo() {
        ValidationRule<Integer> rule = NumberValidationRules.greaterThanOrEqualTo(10);

        assertTrue(rule.validate(15));
        assertTrue(rule.validate(10));
        assertFalse(rule.validate(5));

        assertEquals("This field must be greater than or equal to 10.", rule.getErrorMessage());
    }

    // Test with Double type
    @Test
    void testDoubleLessThan() {
        ValidationRule<Double> rule = NumberValidationRules.lessThan(10.5);

        assertTrue(rule.validate(5.0));
        assertFalse(rule.validate(15.0));

        assertEquals("This field must be less than 10.5.", rule.getErrorMessage());
    }

    @Test
    void testDoubleGreaterThan() {
        ValidationRule<Double> rule = NumberValidationRules.greaterThan(10.5);

        assertTrue(rule.validate(15.0));
        assertFalse(rule.validate(5.0));

        assertEquals("This field must be greater than 10.5.", rule.getErrorMessage());
    }

    @Test
    void testDoubleLessThanOrEqualTo() {
        ValidationRule<Double> rule = NumberValidationRules.lessThanOrEqualTo(10.5);

        assertTrue(rule.validate(5.0));
        assertTrue(rule.validate(10.5));
        assertFalse(rule.validate(15.0));

        assertEquals("This field must be less than or equal to 10.5.", rule.getErrorMessage());
    }

    @Test
    void testDoubleGreaterThanOrEqualTo() {
        ValidationRule<Double> rule = NumberValidationRules.greaterThanOrEqualTo(10.5);

        assertTrue(rule.validate(15.0));
        assertTrue(rule.validate(10.5));
        assertFalse(rule.validate(5.0));

        assertEquals("This field must be greater than or equal to 10.5.", rule.getErrorMessage());
    }
}