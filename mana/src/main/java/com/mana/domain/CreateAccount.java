package com.mana.domain;

import com.mana.domain.models.Account;

public interface CreateAccount {
  public Account create(String firstName, String SecondName, String email, String password);
}
