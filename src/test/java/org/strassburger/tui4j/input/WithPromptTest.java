package org.strassburger.tui4j.input;

import org.junit.jupiter.api.Test;
import org.strassburger.tui4j.formatting.StyledText;

import static org.junit.jupiter.api.Assertions.*;

class WithPromptTest {

    @Test
    void testStringPromptIsSet() {
        assertNotNull(TextInput.withPrompt("Name?").prompt());
        assertNotNull(BooleanInput.withPrompt("Continue?").prompt());
        assertNotNull(ContinueInput.withPrompt("Press enter").prompt());
        assertNotNull(DateInput.withPrompt("Date?").prompt());
        assertNotNull(DoubleInput.withPrompt("Height?").prompt());
        assertNotNull(IntegerInput.withPrompt("Age?").prompt());
        assertNotNull(MultilineTextInput.withPrompt("About?").prompt());
        assertNotNull(SelectInput.<String>withPrompt("Pick one").prompt());
    }

    @Test
    void testStyledPromptIsUsedAsIs() {
        StyledText prompt = StyledText.text("Name?");
        assertSame(prompt, TextInput.withPrompt(prompt).prompt());
        assertSame(prompt, SelectInput.<String>withPrompt(prompt).prompt());
    }

    @Test
    void testNumberInputKeepsType() {
        NumberInput<Long> input = NumberInput.withPrompt(Long.class, "Count?");
        assertEquals(Long.class, input.type());
        assertNotNull(input.prompt());
    }

    @Test
    void testFactoryIsChainable() {
        TextInput input = TextInput.withPrompt("Name?").inline(true).retryOnInvalid(false);
        assertTrue(input.inline());
        assertFalse(input.retryOnInvalid());
    }
}
