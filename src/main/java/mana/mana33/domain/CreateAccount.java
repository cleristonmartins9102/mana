package mana.mana33.domain;

import mana.mana33.domain.models.AccountModel;
import mana.mana33.domain.models.CreateAccountDTO;

public interface CreateAccount {
    AccountModel create(CreateAccountDTO createAccountDTO);
}
