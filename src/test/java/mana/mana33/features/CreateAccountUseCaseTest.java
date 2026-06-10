package mana.mana33.features;

import mana.mana33.domain.Encrypt;
import mana.mana33.domain.Hash;
import mana.mana33.domain.SaveUserRepository;
import mana.mana33.domain.UpdateAccountRepository;
import mana.mana33.domain.models.AccountModel;
import mana.mana33.domain.models.CreateAccountDTO;
import mana.mana33.domain.models.TokenPayloadDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateAccountUseCaseTest {

    @Test
    void shouldCallEncryptWithCorrectPayload() {
        Hash hasher = mock(Hash.class);
        Encrypt encrypter = mock(Encrypt.class);
        SaveUserRepository saveUserRepository = mock(SaveUserRepository.class);
        UpdateAccountRepository updateAccountRepository = mock(UpdateAccountRepository.class);
        Encrypt refreshTokenGenerator = mock(Encrypt.class);

        when(hasher.hash(any())).thenReturn("hashed_password");
        when(encrypter.encrypt(any(TokenPayloadDTO.class))).thenReturn("generated_token");
        when(saveUserRepository.save(any())).thenReturn(new AccountModel("1", "John", "Doe", "john.doe@example.com", "1234567890", "generated_token", null));
        when(refreshTokenGenerator.encrypt(any(TokenPayloadDTO.class))).thenReturn("refresh_token");
        when(updateAccountRepository.update(any(), any())).thenReturn(new AccountModel("1", "John", "Doe", "john.doe@example.com", "1234567890", "generated_token", "refresh_token"));

        CreateAccountUseCase useCase = new CreateAccountUseCase(hasher, encrypter, saveUserRepository, updateAccountRepository, refreshTokenGenerator);
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
        UpdateAccountRepository updateAccountRepository = mock(UpdateAccountRepository.class);
        Encrypt refreshTokenGenerator = mock(Encrypt.class);

        when(hasher.hash("password123")).thenReturn("hashed_password");
        when(encrypter.encrypt(any(TokenPayloadDTO.class))).thenReturn("generated_token");
        when(saveUserRepository.save(any())).thenReturn(new AccountModel("1", "John", "Doe", "john.doe@example.com", "1234567890", "generated_token", null));
        when(refreshTokenGenerator.encrypt(any(TokenPayloadDTO.class))).thenReturn("refresh_token");
        when(updateAccountRepository.update(any(), any())).thenReturn(new AccountModel("1", "John", "Doe", "john.doe@example.com", "1234567890", "generated_token", "refresh_token"));

        CreateAccountUseCase useCase = new CreateAccountUseCase(hasher, encrypter, saveUserRepository, updateAccountRepository, refreshTokenGenerator);
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
        UpdateAccountRepository updateAccountRepository = mock(UpdateAccountRepository.class);
        Encrypt refreshTokenGenerator = mock(Encrypt.class);

        when(hasher.hash("plain_password")).thenReturn("hashed_password");
        when(encrypter.encrypt(any(TokenPayloadDTO.class))).thenReturn("token");
        when(saveUserRepository.save(any())).thenReturn(new AccountModel("1", "Jane", "Smith", "jane@example.com", "9876543210", "token", null));
        when(refreshTokenGenerator.encrypt(any(TokenPayloadDTO.class))).thenReturn("refresh_token");
        when(updateAccountRepository.update(any(), any())).thenReturn(new AccountModel("1", "Jane", "Smith", "jane@example.com", "9876543210", "token", "refresh_token"));

        CreateAccountUseCase useCase = new CreateAccountUseCase(hasher, encrypter, saveUserRepository, updateAccountRepository, refreshTokenGenerator);
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

    @Test
    void shouldSaveGeneratedTokenAsRefreshToken() {
        Hash hasher = mock(Hash.class);
        Encrypt encrypter = mock(Encrypt.class);
        SaveUserRepository saveUserRepository = mock(SaveUserRepository.class);
        UpdateAccountRepository updateAccountRepository = mock(UpdateAccountRepository.class);
        Encrypt refreshTokenGenerator = mock(Encrypt.class);

        when(hasher.hash(any())).thenReturn("hashed");
        when(encrypter.encrypt(any(TokenPayloadDTO.class))).thenReturn("refresh_token_xyz");
        when(saveUserRepository.save(any())).thenReturn(new AccountModel("1", "Bob", "Johnson", "bob@example.com", "5555555555", "refresh_token_xyz", null));
        when(refreshTokenGenerator.encrypt(any(TokenPayloadDTO.class))).thenReturn("refresh_token");
        when(updateAccountRepository.update(any(), any())).thenReturn(new AccountModel("1", "Bob", "Johnson", "bob@example.com", "5555555555", "refresh_token_xyz", "refresh_token"));

        CreateAccountUseCase useCase = new CreateAccountUseCase(hasher, encrypter, saveUserRepository, updateAccountRepository, refreshTokenGenerator);
        CreateAccountDTO dto = new CreateAccountDTO(
            "Bob",
            "Johnson",
            "bob@example.com",
            "password",
            "5555555555"
        );

        useCase.create(dto);

        verify(saveUserRepository).save(argThat(model ->
            model.refreshToken().equals("refresh_token_xyz")
        ));
    }

    @Test
    void shouldCallSaveRepositoryAfterGeneratingToken() {
        Hash hasher = mock(Hash.class);
        Encrypt encrypter = mock(Encrypt.class);
        SaveUserRepository saveUserRepository = mock(SaveUserRepository.class);
        UpdateAccountRepository updateAccountRepository = mock(UpdateAccountRepository.class);
        Encrypt refreshTokenGenerator = mock(Encrypt.class);

        when(hasher.hash(any())).thenReturn("hashed");
        when(encrypter.encrypt(any(TokenPayloadDTO.class))).thenReturn("token");
        when(saveUserRepository.save(any())).thenReturn(new AccountModel("1", "Alice", "Williams", "alice@example.com", "1111111111", "token", null));
        when(refreshTokenGenerator.encrypt(any(TokenPayloadDTO.class))).thenReturn("refresh_token");
        when(updateAccountRepository.update(any(), any())).thenReturn(new AccountModel("1", "Alice", "Williams", "alice@example.com", "1111111111", "token", "refresh_token"));

        CreateAccountUseCase useCase = new CreateAccountUseCase(hasher, encrypter, saveUserRepository, updateAccountRepository, refreshTokenGenerator);
        CreateAccountDTO dto = new CreateAccountDTO(
            "Alice",
            "Williams",
            "alice@example.com",
            "pass",
            "1111111111"
        );

        useCase.create(dto);

        var inOrder = inOrder(encrypter, saveUserRepository);
        inOrder.verify(encrypter).encrypt(any(TokenPayloadDTO.class));
        inOrder.verify(saveUserRepository).save(any());
    }

    @Test
    void shouldGenerateRefreshTokenWithAccountIdAndUpdateRepository() {
        Hash hasher = mock(Hash.class);
        Encrypt encrypter = mock(Encrypt.class);
        SaveUserRepository saveUserRepository = mock(SaveUserRepository.class);
        UpdateAccountRepository updateAccountRepository = mock(UpdateAccountRepository.class);
        Encrypt refreshTokenGenerator = mock(Encrypt.class);

        when(hasher.hash(any())).thenReturn("hashed");
        when(encrypter.encrypt(any(TokenPayloadDTO.class))).thenReturn("token");
        when(saveUserRepository.save(any())).thenReturn(new AccountModel("123", "Test", "User", "test@example.com", "1234567890", "token", null));
        when(refreshTokenGenerator.encrypt(any(TokenPayloadDTO.class))).thenReturn("refresh_token_with_id");
        when(updateAccountRepository.update(any(), any())).thenReturn(new AccountModel("123", "Test", "User", "test@example.com", "1234567890", "token", "refresh_token_with_id"));

        CreateAccountUseCase useCase = new CreateAccountUseCase(hasher, encrypter, saveUserRepository, updateAccountRepository, refreshTokenGenerator);
        CreateAccountDTO dto = new CreateAccountDTO(
            "Test",
            "User",
            "test@example.com",
            "password",
            "1234567890"
        );

        useCase.create(dto);

        verify(refreshTokenGenerator).encrypt(argThat(payload ->
            payload.getId().equals("123") &&
            payload.getFirstName().equals("Test") &&
            payload.getLastName().equals("User") &&
            payload.getEmail().equals("test@example.com")
        ));

        verify(updateAccountRepository).update(eq("123"), argThat(model ->
            model.id().equals("123") &&
            model.refreshToken().equals("refresh_token_with_id")
        ));
    }

    @Test
    void shouldReturnCreatedAccountModelWithRefreshToken() {
        Hash hasher = mock(Hash.class);
        Encrypt encrypter = mock(Encrypt.class);
        SaveUserRepository saveUserRepository = mock(SaveUserRepository.class);
        UpdateAccountRepository updateAccountRepository = mock(UpdateAccountRepository.class);
        Encrypt refreshTokenGenerator = mock(Encrypt.class);

        when(hasher.hash(any())).thenReturn("hashed");
        when(encrypter.encrypt(any(TokenPayloadDTO.class))).thenReturn("access_token");
        when(saveUserRepository.save(any())).thenReturn(new AccountModel("456", "John", "Smith", "john@example.com", "9999999999", "access_token", null));
        when(refreshTokenGenerator.encrypt(any(TokenPayloadDTO.class))).thenReturn("refresh_token_456");
        when(updateAccountRepository.update(any(), any())).thenReturn(new AccountModel("456", "John", "Smith", "john@example.com", "9999999999", "access_token", "refresh_token_456"));

        CreateAccountUseCase useCase = new CreateAccountUseCase(hasher, encrypter, saveUserRepository, updateAccountRepository, refreshTokenGenerator);
        CreateAccountDTO dto = new CreateAccountDTO(
            "John",
            "Smith",
            "john@example.com",
            "password123",
            "9999999999"
        );

        AccountModel result = useCase.create(dto);

        assert result != null;
        assert result.id().equals("456");
        assert result.firstName().equals("John");
        assert result.secondName().equals("Smith");
        assert result.email().equals("john@example.com");
        assert result.mobileNumber().equals("9999999999");
        assert result.token().equals("access_token");
        assert result.refreshToken().equals("refresh_token_456");
    }
}
