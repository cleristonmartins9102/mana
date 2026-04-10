package com.mana.features;

import com.mana.domain.CreateAccount;
import com.mana.domain.Encrypt;
import com.mana.domain.Hash;
import com.mana.domain.models.Account;
import com.mana.domain.models.TokenPayload;

public class CreateAccountUseCase implements CreateAccount{
  private Hash bcryptAdapter;
  private Encrypt jsonWebTokenAdapter;

  public CreateAccountUseCase(Hash bcryptAdapter, Encrypt jsonWebTokenAdapter) {
    this.bcryptAdapter = bcryptAdapter;
    this.jsonWebTokenAdapter = jsonWebTokenAdapter;
  }

  @Override
  public Account create(String firstName, String SecondName, String email, String password) {
    final String hashPassword = this.bcryptAdapter.hash(password);
    final TokenPayload token = new TokenPayload("", "", "");
    final String token = this.jsonWebTokenAdapter.encrypt(null)
    return null;
  }
}
