package mana.mana33.domain;

import mana.mana33.domain.models.CreateAccountDTO;

public interface SaveUserRepository {
    void save(CreateAccountDTO account);
}
