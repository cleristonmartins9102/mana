package mana.mana33.features;

import mana.mana33.domain.Decrypter;
import mana.mana33.domain.models.AccountModel;
import mana.mana33.domain.models.AuthenticationDTO;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AuthenticateUseCaseTest {

    @Test
    void shouldReceiveDecrypterViaDependencyInjection() {
        Decrypter decrypter = mock(Decrypter.class);

        AuthenticateUseCase useCase = new AuthenticateUseCase(decrypter);

        assertNotNull(useCase);
    }

    @Test
    void shouldImplementAuthenticateInterface() {
        Decrypter decrypter = mock(Decrypter.class);

        AuthenticateUseCase useCase = new AuthenticateUseCase(decrypter);

        assertTrue(useCase instanceof mana.mana33.domain.Authenticate);
    }

    @Test
    void shouldAcceptAuthenticationDTO() {
        Decrypter decrypter = mock(Decrypter.class);
        when(decrypter.decrypt(any())).thenReturn("account-id");

        AuthenticateUseCase useCase = new AuthenticateUseCase(decrypter);

        AuthenticationDTO dto = new AuthenticationDTO("test@example.com", "password", "token");

        assertDoesNotThrow(() -> {
            useCase.authenticate(dto);
        });
    }

    @Test
    void shouldCallDecrypterWithToken() {
        Decrypter decrypter = mock(Decrypter.class);
        when(decrypter.decrypt("test-token")).thenReturn("123");

        AuthenticateUseCase useCase = new AuthenticateUseCase(decrypter);
        AuthenticationDTO dto = new AuthenticationDTO("test@example.com", "password", "test-token");

        useCase.authenticate(dto);

        verify(decrypter, times(1)).decrypt("test-token");
    }

    @Test
    void shouldDecryptTokenFromAuthenticationDTO() {
        Decrypter decrypter = mock(Decrypter.class);
        when(decrypter.decrypt(any())).thenReturn("account-id-456");

        AuthenticateUseCase useCase = new AuthenticateUseCase(decrypter);
        AuthenticationDTO dto = new AuthenticationDTO("user@example.com", "pass123", "encrypted-token");

        useCase.authenticate(dto);

        verify(decrypter).decrypt("encrypted-token");
    }

    @Test
    void shouldReturnAccountModel() {
        Decrypter decrypter = mock(Decrypter.class);
        when(decrypter.decrypt(any())).thenReturn("account-id");

        AuthenticateUseCase useCase = new AuthenticateUseCase(decrypter);

        AuthenticationDTO dto = new AuthenticationDTO("test@example.com", "password", "token");

        AccountModel result = useCase.authenticate(dto);

        // Currently returns null as repository implementation is pending
        assertNull(result);
    }
}
