package com.mana.domain;

import com.mana.domain.models.Account;
import com.mana.domain.models.CreateAccountDTO;

public interface CreateAccount {
  public Account create(CreateAccountDTO createAccountData);
}
