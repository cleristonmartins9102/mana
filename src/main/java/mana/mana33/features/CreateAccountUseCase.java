package mana.mana33.features;

import mana.mana33.domain.CreateAccount;
import mana.mana33.domain.Encrypt;
import mana.mana33.domain.Hash;
import mana.mana33.domain.models.CreateAccountDTO;
import mana.mana33.domain.models.TokenPayloadDTO;

public class CreateAccountUseCase implements CreateAccount {
    private final Hash hasher;
    private final Encrypt encrypter;

    public  CreateAccountUseCase(Hash hasher, Encrypt encrypter) {
        this.hasher = hasher;
        this.encrypter = encrypter;
    }

    @Override
    public void create(CreateAccountDTO createAccountDTO) {
        String hashedPassword = this.hasher.hash(createAccountDTO.password());

        TokenPayloadDTO payload = new TokenPayloadDTO();
        payload.firstName = createAccountDTO.firstName();
        payload.lastName = createAccountDTO.secondName();
        payload.email = createAccountDTO.email();

        String token = this.encrypter.encrypt(payload);
    }
}
