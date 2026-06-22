package mana.mana33.domain;

import mana.mana33.domain.models.AccountModel;
import mana.mana33.domain.models.AuthenticationDTO;

public interface Authenticate {
    AccountModel authenticate(AuthenticationDTO authenticationDTO);
}
