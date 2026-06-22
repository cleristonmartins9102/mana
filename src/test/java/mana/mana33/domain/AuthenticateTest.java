package mana.mana33.domain;

import mana.mana33.domain.models.AccountModel;
import mana.mana33.domain.models.AuthenticationDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticateTest {

    private static class TestAuthenticateImplementation implements Authenticate {
        @Override
        public AccountModel authenticate(AuthenticationDTO authenticationDTO) {
            if (authenticationDTO.email().equals("test@example.com") &&
                authenticationDTO.password().equals("password123")) {
                return new AccountModel(
                    "1",
                    "Test",
                    "User",
                    "test@example.com",
                    "1234567890",
                    "access_token",
                    "refresh_token"
                );
            }
            throw new RuntimeException("Invalid credentials");
        }
    }

    @Test
    void shouldReturnAccountModelWhenAuthenticationSucceeds() {
        Authenticate authenticate = new TestAuthenticateImplementation();
        AuthenticationDTO dto = new AuthenticationDTO("test@example.com", "password123");

        AccountModel result = authenticate.authenticate(dto);

        assertNotNull(result);
        assertEquals("1", result.id());
        assertEquals("Test", result.firstName());
        assertEquals("test@example.com", result.email());
        assertEquals("access_token", result.token());
        assertEquals("refresh_token", result.refreshToken());
    }

    @Test
    void shouldThrowExceptionWhenCredentialsAreInvalid() {
        Authenticate authenticate = new TestAuthenticateImplementation();
        AuthenticationDTO dto = new AuthenticationDTO("wrong@example.com", "wrongpassword");

        assertThrows(RuntimeException.class, () -> {
            authenticate.authenticate(dto);
        });
    }

    @Test
    void shouldAcceptAuthenticationDTO() {
        Authenticate authenticate = new TestAuthenticateImplementation();
        AuthenticationDTO dto = new AuthenticationDTO("test@example.com", "password123");

        assertDoesNotThrow(() -> {
            authenticate.authenticate(dto);
        });
    }

    @Test
    void shouldReturnAccountWithTokens() {
        Authenticate authenticate = new TestAuthenticateImplementation();
        AuthenticationDTO dto = new AuthenticationDTO("test@example.com", "password123");

        AccountModel result = authenticate.authenticate(dto);

        assertNotNull(result.token());
        assertNotNull(result.refreshToken());
    }
}
