package mana.mana33.domain;

import mana.mana33.domain.models.AccountModel;
import mana.mana33.domain.models.SaveUserModel;

public interface SaveUserRepository {
    public AccountModel save(SaveUserModel model);
}
