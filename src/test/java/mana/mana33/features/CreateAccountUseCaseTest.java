package mana.mana33.features;

import mana.mana33.domain.Encrypt;
import mana.mana33.domain.Hash;
import mana.mana33.domain.SaveUserRepository;
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
        SaveUserRepository saveUserRepository = mock(SaveUserRepository.class);

        when(hasher.hash(any())).thenReturn("hashed_password");
        when(encrypter.encrypt(any(TokenPayloadDTO.class))).thenReturn("generated_token");

        CreateAccountUseCase useCase = new CreateAccountUseCase(hasher, encrypter, saveUserRepository);
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

    @Test
    void shouldCallSaveUserRepositoryWithCorrectData() {
        Hash hasher = mock(Hash.class);
        Encrypt encrypter = mock(Encrypt.class);
        SaveUserRepository saveUserRepository = mock(SaveUserRepository.class);

        when(hasher.hash("password123")).thenReturn("hashed_password");
        when(encrypter.encrypt(any(TokenPayloadDTO.class))).thenReturn("generated_token");

        CreateAccountUseCase useCase = new CreateAccountUseCase(hasher, encrypter, saveUserRepository);
        CreateAccountDTO dto = new CreateAccountDTO(
            "John",
            "Doe",
            "john.doe@example.com",
            "password123",
            "1234567890"
        );

        useCase.create(dto);

        verify(saveUserRepository, times(1)).save(argThat(model ->
            model.firstName().equals("John") &&
            model.secondName().equals("Doe") &&
            model.email().equals("john.doe@example.com") &&
            model.password().equals("hashed_password") &&
            model.mobileNumber().equals("1234567890") &&
            model.refreshToken().equals("generated_token")
        ));
    }

    @Test
    void shouldSaveHashedPasswordNotPlainPassword() {
        Hash hasher = mock(Hash.class);
        Encrypt encrypter = mock(Encrypt.class);
        SaveUserRepository saveUserRepository = mock(SaveUserRepository.class);

        when(hasher.hash("plain_password")).thenReturn("hashed_password");
        when(encrypter.encrypt(any(TokenPayloadDTO.class))).thenReturn("token");

        CreateAccountUseCase useCase = new CreateAccountUseCase(hasher, encrypter, saveUserRepository);
        CreateAccountDTO dto = new CreateAccountDTO(
            "Jane",
            "Smith",
            "jane@example.com",
            "plain_password",
            "9876543210"
        );

        useCase.create(dto);

        verify(saveUserRepository).save(argThat(model ->
            model.password().equals("hashed_password") &&
            !model.password().equals("plain_password")
        ));
    }
}
