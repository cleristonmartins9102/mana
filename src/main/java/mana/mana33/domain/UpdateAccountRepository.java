package mana.mana33.domain;

import mana.mana33.domain.models.AccountModel;

public interface UpdateAccountRepository {
    public AccountModel update(String id, AccountModel model);
}
