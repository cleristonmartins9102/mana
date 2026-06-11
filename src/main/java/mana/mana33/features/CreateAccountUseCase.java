package mana.mana33.features;

import mana.mana33.domain.CreateAccount;
import mana.mana33.domain.Encrypt;
import mana.mana33.domain.Hash;
import mana.mana33.domain.SaveUserRepository;
import mana.mana33.domain.UpdateAccountRepository;
import mana.mana33.domain.exceptions.TokenGenerationException;
import mana.mana33.domain.models.AccountModel;
import mana.mana33.domain.models.CreateAccountDTO;
import mana.mana33.domain.models.SaveUserModel;
import mana.mana33.domain.models.TokenPayloadDTO;

public class CreateAccountUseCase implements CreateAccount {
    private final Hash hasher;
    private final Encrypt encrypter;
    private final SaveUserRepository saveUserRepository;
    private final UpdateAccountRepository updateAccountRepository;
    private final Encrypt refreshTokenGenerator;

    public  CreateAccountUseCase(Hash hasher, Encrypt encrypter, SaveUserRepository saveUserRepository, UpdateAccountRepository updateAccountRepository, Encrypt refreshTokenGenerator) {
        this.hasher = hasher;
        this.encrypter = encrypter;
        this.saveUserRepository = saveUserRepository;
        this.updateAccountRepository = updateAccountRepository;
        this.refreshTokenGenerator = refreshTokenGenerator;
    }

    @Override
    public AccountModel create(CreateAccountDTO createAccountDTO) {
        String hashedPassword = this.hasher.hash(createAccountDTO.password());

        TokenPayloadDTO payload = new TokenPayloadDTO();
        payload.firstName = createAccountDTO.firstName();
        payload.lastName = createAccountDTO.secondName();
        payload.email = createAccountDTO.email();

        String token;
        try {
            token = this.encrypter.encrypt(payload);
        } catch (Exception e) {
            throw new TokenGenerationException("Failed to generate access token", e);
        }

        SaveUserModel saveUserModel = new SaveUserModel(
                createAccountDTO.firstName(),
                createAccountDTO.secondName(),
                createAccountDTO.email(),
                hashedPassword,
                createAccountDTO.mobileNumber(),
                token
        );

        AccountModel savedAccount = this.saveUserRepository.save(saveUserModel);

        TokenPayloadDTO refreshPayload = new TokenPayloadDTO();
        refreshPayload.id = savedAccount.id();
        refreshPayload.firstName = savedAccount.firstName();
        refreshPayload.lastName = savedAccount.secondName();
        refreshPayload.email = savedAccount.email();

        String refreshToken;
        try {
            refreshToken = this.refreshTokenGenerator.encrypt(refreshPayload);
        } catch (Exception e) {
            throw new TokenGenerationException("Failed to generate refresh token for account: " + savedAccount.id(), e);
        }

        AccountModel updatedAccount = new AccountModel(
                savedAccount.id(),
                savedAccount.firstName(),
                savedAccount.secondName(),
                savedAccount.email(),
                savedAccount.mobileNumber(),
                savedAccount.token(),
                refreshToken
        );

        return this.updateAccountRepository.update(savedAccount.id(), updatedAccount);
    }
}
