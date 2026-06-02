package mana33.controller;

import mana.mana33.domain.models.CreateAccountDTO;
import mana.mana33.controller.contracts.Controller;
import mana.mana33.controller.http.HttpRequest;
import mana.mana33.controller.http.HttpResponse;

public class CreateAccountController
        extends Controller<String, CreateAccountDTO, Void> {

    @Override
    public HttpResponse<String> perform(
            HttpRequest<CreateAccountDTO, Void> input) {
        return null;
    }
}

