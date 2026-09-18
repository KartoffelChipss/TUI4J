package org.strassburger.tui4j.input.validationrules;

import org.junit.jupiter.api.Test;
import org.strassburger.tui4j.formatting.PlainTextRenderer;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DateValidationRulesTest {
    private final PlainTextRenderer renderer = new PlainTextRenderer();

    @Test
    void testFutureDate() {
        ValidationRule<LocalDate> rule = DateValidationRules.futureDate();

        LocalDate futureDate = LocalDate.now().plusDays(1); // Future date
        LocalDate pastDate = LocalDate.now().minusDays(1); // Past date

        assertTrue(rule.validate(futureDate));
        assertFalse(rule.validate(pastDate));
        assertEquals("The date must be in the future.", renderer.render(rule.getErrorMessage()));
    }

    @Test
    void testPastDate() {
        ValidationRule<LocalDate> rule = DateValidationRules.pastDate();

        LocalDate futureDate = LocalDate.now().plusDays(1); // Future date
        LocalDate pastDate = LocalDate.now().minusDays(1); // Past date

        assertTrue(rule.validate(pastDate));
        assertFalse(rule.validate(futureDate));
        assertEquals("The date must be in the past.", renderer.render(rule.getErrorMessage()));
    }

    @Test
    void testAfter() {
        LocalDate comparisonDate = LocalDate.now();
        ValidationRule<LocalDate> rule = DateValidationRules.after(comparisonDate);

        LocalDate futureDate = comparisonDate.plusDays(1); // After comparisonDate
        LocalDate pastDate = comparisonDate.minusDays(1); // Before comparisonDate

        assertTrue(rule.validate(futureDate));
        assertFalse(rule.validate(pastDate));
        assertEquals("The date must be after " + comparisonDate + ".", renderer.render(rule.getErrorMessage()));
    }

    @Test
    void testBefore() {
        LocalDate comparisonDate = LocalDate.now();
        ValidationRule<LocalDate> rule = DateValidationRules.before(comparisonDate);

        LocalDate futureDate = comparisonDate.plusDays(1); // After comparisonDate
        LocalDate pastDate = comparisonDate.minusDays(1); // Before comparisonDate

        assertTrue(rule.validate(pastDate));
        assertFalse(rule.validate(futureDate));
        assertEquals("The date must be before " + comparisonDate + ".", renderer.render(rule.getErrorMessage()));
    }
}