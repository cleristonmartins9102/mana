package mana.mana33.domain;

import mana.mana33.domain.models.TokenPayloadDTO;

public interface Encrypt {
    public String encrypt(TokenPayloadDTO payload);
}
