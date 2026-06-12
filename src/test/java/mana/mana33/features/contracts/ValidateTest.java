package mana.mana33.features.contracts;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidateTest {

    private static class TestDTO {
        String name;
        int age;

        TestDTO(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }

    private static class TestValidator implements Validate<TestDTO> {
        private boolean validateCalled = false;

        @Override
        public void validate(TestDTO input) {
            validateCalled = true;
            if (input == null) {
                throw new IllegalArgumentException("Input cannot be null");
            }
            if (input.name == null || input.name.isEmpty()) {
                throw new IllegalArgumentException("Name cannot be empty");
            }
            if (input.age < 0) {
                throw new IllegalArgumentException("Age cannot be negative");
            }
        }

        boolean wasValidateCalled() {
            return validateCalled;
        }
    }

    @Test
    void shouldCallValidateMethod() {
        TestValidator validator = new TestValidator();
        TestDTO dto = new TestDTO("John", 25);

        validator.validate(dto);

        assertTrue(validator.wasValidateCalled());
    }

    @Test
    void shouldThrowExceptionWhenInputIsNull() {
        TestValidator validator = new TestValidator();

        assertThrows(IllegalArgumentException.class, () -> {
            validator.validate(null);
        });
    }

    @Test
    void shouldThrowExceptionWhenNameIsEmpty() {
        TestValidator validator = new TestValidator();
        TestDTO dto = new TestDTO("", 25);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            validator.validate(dto);
        });

        assertEquals("Name cannot be empty", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAgeIsNegative() {
        TestValidator validator = new TestValidator();
        TestDTO dto = new TestDTO("John", -1);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            validator.validate(dto);
        });

        assertEquals("Age cannot be negative", exception.getMessage());
    }

    @Test
    void shouldNotThrowExceptionWhenInputIsValid() {
        TestValidator validator = new TestValidator();
        TestDTO dto = new TestDTO("John", 25);

        assertDoesNotThrow(() -> {
            validator.validate(dto);
        });
    }
}
