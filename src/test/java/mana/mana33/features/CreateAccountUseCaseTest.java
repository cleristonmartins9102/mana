package mana.mana33.features;

import mana.mana33.domain.Encrypt;
import mana.mana33.domain.Hash;
import mana.mana33.domain.models.CreateAccountDTO;
import mana.mana33.domain.models.TokenPayloadDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateAccountUseCaseTest {

    @Test
    void shouldCallEncryptWithCorrectPayload() {
        Hash hasher = mock(Hash.class);
        Encrypt encrypter = mock(Encrypt.class);

        when(hasher.hash(any())).thenReturn("hashed_password");
        when(encrypter.encrypt(any(TokenPayloadDTO.class))).thenReturn("generated_token");

        CreateAccountUseCase useCase = new CreateAccountUseCase(hasher, encrypter);
        CreateAccountDTO dto = new CreateAccountDTO(
            "John",
            "Doe",
            "john.doe@example.com",
            "password123",
            "1234567890"
        );

        useCase.create(dto);

        verify(encrypter, times(1)).encrypt(argThat(payload ->
            payload.getFirstName().equals("John") &&
            payload.getLastName().equals("Doe") &&
            payload.getEmail().equals("john.doe@example.com")
        ));
    }
}
