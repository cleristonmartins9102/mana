package mana.mana33.validators;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HibernateValidatorAdapterTest {

    private Validator validator;
    private HibernateValidatorAdapter<TestDTO> adapter;

    private static class TestDTO {
        @NotNull(message = "Name cannot be null")
        @NotBlank(message = "Name cannot be blank")
        private String name;

        @Email(message = "Invalid email format")
        @NotBlank(message = "Email cannot be blank")
        private String email;

        @Size(min = 3, max = 10, message = "Username must be between 3 and 10 characters")
        private String username;

        public TestDTO(String name, String email, String username) {
            this.name = name;
            this.email = email;
            this.username = username;
        }
    }

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        adapter = new HibernateValidatorAdapter<>(validator);
    }

    @Test
    void shouldThrowExceptionWhenInputIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adapter.validate(null);
        });

        assertEquals("Input cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        TestDTO dto = new TestDTO(null, "test@example.com", "user123");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adapter.validate(dto);
        });

        assertTrue(exception.getMessage().contains("Name cannot be null"));
    }

    @Test
    void shouldThrowExceptionWhenNameIsBlank() {
        TestDTO dto = new TestDTO("", "test@example.com", "user123");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adapter.validate(dto);
        });

        assertTrue(exception.getMessage().contains("Name cannot be blank"));
    }

    @Test
    void shouldThrowExceptionWhenEmailIsInvalid() {
        TestDTO dto = new TestDTO("John Doe", "invalid-email", "user123");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adapter.validate(dto);
        });

        assertTrue(exception.getMessage().contains("Invalid email format"));
    }

    @Test
    void shouldThrowExceptionWhenEmailIsBlank() {
        TestDTO dto = new TestDTO("John Doe", "", "user123");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adapter.validate(dto);
        });

        assertTrue(exception.getMessage().contains("Email cannot be blank"));
    }

    @Test
    void shouldThrowExceptionWhenUsernameIsTooShort() {
        TestDTO dto = new TestDTO("John Doe", "test@example.com", "ab");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adapter.validate(dto);
        });

        assertTrue(exception.getMessage().contains("Username must be between 3 and 10 characters"));
    }

    @Test
    void shouldThrowExceptionWhenUsernameIsTooLong() {
        TestDTO dto = new TestDTO("John Doe", "test@example.com", "verylongusername");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adapter.validate(dto);
        });

        assertTrue(exception.getMessage().contains("Username must be between 3 and 10 characters"));
    }

    @Test
    void shouldThrowExceptionWithMultipleViolations() {
        TestDTO dto = new TestDTO("", "invalid-email", "ab");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adapter.validate(dto);
        });

        String message = exception.getMessage();
        assertTrue(message.contains("Name cannot be blank") ||
                   message.contains("Invalid email format") ||
                   message.contains("Username must be between 3 and 10 characters"));
    }

    @Test
    void shouldNotThrowExceptionWhenDTOIsValid() {
        TestDTO dto = new TestDTO("John Doe", "john.doe@example.com", "johndoe");

        assertDoesNotThrow(() -> {
            adapter.validate(dto);
        });
    }

    @Test
    void shouldNotThrowExceptionWithMinimumValidUsername() {
        TestDTO dto = new TestDTO("John Doe", "test@example.com", "abc");

        assertDoesNotThrow(() -> {
            adapter.validate(dto);
        });
    }

    @Test
    void shouldNotThrowExceptionWithMaximumValidUsername() {
        TestDTO dto = new TestDTO("John Doe", "test@example.com", "abcdefghij");

        assertDoesNotThrow(() -> {
            adapter.validate(dto);
        });
    }
}
