package com.mana.domain;

import com.mana.domain.models.TokenPayload;

public interface Encrypt {
  public String encrypt(TokenPayload payload);
}
