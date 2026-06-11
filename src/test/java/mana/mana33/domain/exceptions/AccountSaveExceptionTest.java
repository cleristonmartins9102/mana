package mana.mana33.domain.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountSaveExceptionTest {

    @Test
    void shouldCreateExceptionWithMessage() {
        String message = "Account save failed";
        AccountSaveException exception = new AccountSaveException(message);

        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void shouldCreateExceptionWithMessageAndCause() {
        String message = "Account save failed";
        Throwable cause = new RuntimeException("Database error");
        AccountSaveException exception = new AccountSaveException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void shouldBeRuntimeException() {
        AccountSaveException exception = new AccountSaveException("Error");

        assertTrue(exception instanceof RuntimeException);
    }
}
