package mana.mana33.features;

import mana.mana33.domain.Authenticate;
import mana.mana33.domain.Decrypter;
import mana.mana33.domain.models.AccountModel;
import mana.mana33.domain.models.AuthenticationDTO;

public class AuthenticateUseCase implements Authenticate {

    private final Decrypter decrypter;

    public AuthenticateUseCase(Decrypter decrypter) {
        this.decrypter = decrypter;
    }

    @Override
    public AccountModel authenticate(AuthenticationDTO authenticationDTO) {
        // TODO: Implement authentication logic
        return null;
    }
}
