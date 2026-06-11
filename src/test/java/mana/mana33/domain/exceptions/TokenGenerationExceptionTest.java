package mana.mana33.domain.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenGenerationExceptionTest {

    @Test
    void shouldCreateExceptionWithMessage() {
        String message = "Token generation failed";
        TokenGenerationException exception = new TokenGenerationException(message);

        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void shouldCreateExceptionWithMessageAndCause() {
        String message = "Token generation failed";
        Throwable cause = new RuntimeException("Underlying error");
        TokenGenerationException exception = new TokenGenerationException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void shouldBeRuntimeException() {
        TokenGenerationException exception = new TokenGenerationException("Error");

        assertTrue(exception instanceof RuntimeException);
    }
}
