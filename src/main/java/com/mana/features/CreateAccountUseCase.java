package com.mana.features;

import com.mana.domain.CreateAccount;
import com.mana.domain.Encrypt;
import com.mana.domain.Hash;
import com.mana.domain.models.Account;
import com.mana.domain.models.CreateAccountDTO;
import com.mana.domain.models.TokenPayload;
import com.mana.features.contract.SaveAccountRepository;

public class CreateAccountUseCase implements CreateAccount{
  private Hash bcryptAdapter;
  private Encrypt jsonWebTokenAdapter;
  private SaveAccountRepository accountRepository;

  public CreateAccountUseCase(Hash bcryptAdapter, Encrypt jsonWebTokenAdapter, SaveAccountRepository accountRepository) {
    this.bcryptAdapter = bcryptAdapter;
    this.jsonWebTokenAdapter = jsonWebTokenAdapter;
    this.accountRepository = accountRepository;
  }

  @Override
  public Account create(CreateAccountDTO createAccountData) {
    final String hashPassword = this.bcryptAdapter.hash(createAccountData.password());
    final TokenPayload payload = new TokenPayload(createAccountData., secondName, email);
    final String token = this.jsonWebTokenAdapter.encrypt(payload);
    final String accountId = this.accountRepository.save(firstName);
    return null;
  }
}
