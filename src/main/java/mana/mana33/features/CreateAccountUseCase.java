package mana.mana33.features;

import mana.mana33.domain.CreateAccount;
import mana.mana33.domain.Encrypt;
import mana.mana33.domain.Hash;
import mana.mana33.domain.SaveUserRepository;
import mana.mana33.domain.models.CreateAccountDTO;
import mana.mana33.domain.models.SaveUserModel;
import mana.mana33.domain.models.TokenPayloadDTO;

public class CreateAccountUseCase implements CreateAccount {
    private final Hash hasher;
    private final Encrypt encrypter;
    private final SaveUserRepository saveUserRepository;

    public  CreateAccountUseCase(Hash hasher, Encrypt encrypter, SaveUserRepository saveUserRepository) {
        this.hasher = hasher;
        this.encrypter = encrypter;
        this.saveUserRepository = saveUserRepository;
    }

    @Override
    public void create(CreateAccountDTO createAccountDTO) {
        String hashedPassword = this.hasher.hash(createAccountDTO.password());

        TokenPayloadDTO payload = new TokenPayloadDTO();
        payload.firstName = createAccountDTO.firstName();
        payload.lastName = createAccountDTO.secondName();
        payload.email = createAccountDTO.email();

        String token = this.encrypter.encrypt(payload);

        SaveUserModel saveUserModel = new SaveUserModel(
                createAccountDTO.firstName(),
                createAccountDTO.secondName(),
                createAccountDTO.email(),
                hashedPassword,
                createAccountDTO.mobileNumber(),
                token
        );

        this.saveUserRepository.save(saveUserModel);
    }
}
