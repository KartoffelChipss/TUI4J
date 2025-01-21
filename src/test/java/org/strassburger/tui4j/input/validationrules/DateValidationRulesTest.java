package org.strassburger.tui4j.input.validationrules;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class DateValidationRulesTest {

    @Test
    void testFutureDate() {
        ValidationRule<Date> rule = DateValidationRules.futureDate();

        Date futureDate = new Date(System.currentTimeMillis() + 100000); // Future date
        Date pastDate = new Date(System.currentTimeMillis() - 100000); // Past date

        assertTrue(rule.validate(futureDate));
        assertFalse(rule.validate(pastDate));
        assertEquals("The date must be in the future.", rule.getErrorMessage().replace("&c", ""));
    }

    @Test
    void testPastDate() {
        ValidationRule<Date> rule = DateValidationRules.pastDate();

        Date futureDate = new Date(System.currentTimeMillis() + 100000); // Future date
        Date pastDate = new Date(System.currentTimeMillis() - 100000); // Past date

        assertTrue(rule.validate(pastDate));
        assertFalse(rule.validate(futureDate));
        assertEquals("The date must be in the past.", rule.getErrorMessage().replace("&c", ""));
    }

    @Test
    void testAfter() {
        Date comparisonDate = new Date(System.currentTimeMillis());
        ValidationRule<Date> rule = DateValidationRules.after(comparisonDate);

        Date futureDate = new Date(comparisonDate.getTime() + 100000); // After comparisonDate
        Date pastDate = new Date(comparisonDate.getTime() - 100000); // Before comparisonDate

        assertTrue(rule.validate(futureDate));
        assertFalse(rule.validate(pastDate));
        assertEquals("The date must be after " + comparisonDate + ".", rule.getErrorMessage().replace("&c", ""));
    }

    @Test
    void testBefore() {
        Date comparisonDate = new Date(System.currentTimeMillis());
        ValidationRule<Date> rule = DateValidationRules.before(comparisonDate);

        Date futureDate = new Date(comparisonDate.getTime() + 100000); // After comparisonDate
        Date pastDate = new Date(comparisonDate.getTime() - 100000); // Before comparisonDate

        assertTrue(rule.validate(pastDate));
        assertFalse(rule.validate(futureDate));
        assertEquals("The date must be before " + comparisonDate + ".", rule.getErrorMessage().replace("&c", ""));
    }
}
