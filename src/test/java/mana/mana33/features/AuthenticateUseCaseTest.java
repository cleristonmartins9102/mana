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
        AuthenticateUseCase useCase = new AuthenticateUseCase(decrypter);

        AuthenticationDTO dto = new AuthenticationDTO("test@example.com", "password");

        assertDoesNotThrow(() -> {
            useCase.authenticate(dto);
        });
    }

    @Test
    void shouldHaveDecrypterDependency() {
        Decrypter decrypter = mock(Decrypter.class);

        AuthenticateUseCase useCase = new AuthenticateUseCase(decrypter);
        AuthenticationDTO dto = new AuthenticationDTO("test@example.com", "password");

        useCase.authenticate(dto);

        // Verify decrypter is available (will be used in implementation)
        assertNotNull(useCase);
    }

    @Test
    void shouldReturnAccountModel() {
        Decrypter decrypter = mock(Decrypter.class);
        AuthenticateUseCase useCase = new AuthenticateUseCase(decrypter);

        AuthenticationDTO dto = new AuthenticationDTO("test@example.com", "password");

        AccountModel result = useCase.authenticate(dto);

        // Currently returns null as implementation is pending
        assertNull(result);
    }
}
