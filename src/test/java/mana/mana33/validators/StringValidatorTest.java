package mana.mana33.validators;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringValidatorTest {

    @Test
    void shouldThrowExceptionWhenStringIsNull() {
        StringValidator validator = new StringValidator();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            validator.validate(null);
        });

        assertEquals("String cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenStringIsEmpty() {
        StringValidator validator = new StringValidator();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            validator.validate("");
        });

        assertEquals("String cannot be empty", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenStringIsOnlyWhitespace() {
        StringValidator validator = new StringValidator();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            validator.validate("   ");
        });

        assertEquals("String cannot be empty", exception.getMessage());
    }

    @Test
    void shouldNotThrowExceptionWhenStringIsValid() {
        StringValidator validator = new StringValidator();

        assertDoesNotThrow(() -> {
            validator.validate("Valid String");
        });
    }

    @Test
    void shouldNotThrowExceptionWhenStringHasLeadingAndTrailingSpaces() {
        StringValidator validator = new StringValidator();

        assertDoesNotThrow(() -> {
            validator.validate("  Valid String  ");
        });
    }

    @Test
    void shouldNotThrowExceptionForSingleCharacter() {
        StringValidator validator = new StringValidator();

        assertDoesNotThrow(() -> {
            validator.validate("A");
        });
    }

    @Test
    void shouldValidateThatInputIsString() {
        StringValidator validator = new StringValidator();
        String validString = "Test String";

        assertDoesNotThrow(() -> {
            validator.validate(validString);
        });

        assertTrue(validString instanceof String);
    }
}
