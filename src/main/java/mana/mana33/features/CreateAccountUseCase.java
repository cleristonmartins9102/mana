package mana.mana33.features;

import mana.mana33.domain.CreateAccount;
import mana.mana33.domain.Hash;
import mana.mana33.domain.models.CreateAccountDTO;

public class CreateAccountUseCase implements CreateAccount {
    private final Hash hasher;

    public  CreateAccountUseCase(Hash hasher) {
        this.hasher = hasher;
    }

    @Override
    public void create(CreateAccountDTO createAccountDTO) {
        String token = this.hasher.hash(createAccountDTO.password());
    }
}
