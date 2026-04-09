package com.mana.features;

import com.mana.domain.CreateAccount;
import com.mana.domain.Encrypt;
import com.mana.domain.Hash;
import com.mana.domain.models.Account;

public class CreateAccountUseCase implements CreateAccount{
  private Hash bcryptAdapter;
  private Encrypt jsonWebTokenAdapter;
  public void CreateAccountUseCase(Hash bcryptAdapter, Encrypt jsonWebTokenAdapter) {
    this.bcryptAdapter = bcryptAdapter;
    this.jsonWebTokenAdapter = jsonWebTokenAdapter;
  }

  @Override
  public Account create(String firstName, String SecondName, String email, String password) {
    final String hashPassword = this.bcryptAdapter.hash(password);
    return null;
  }
}
