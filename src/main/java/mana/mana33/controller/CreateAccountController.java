package mana.mana33.controller;

import mana.mana33.domain.models.CreateAccountDTO;
import mana.mana33.controller.contracts.Controller;
import mana.mana33.controller.http.HttpRequest;
import mana.mana33.controller.http.HttpResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
public class CreateAccountController
        extends Controller<String, CreateAccountDTO, Void> {

    @PostMapping
    public HttpResponse<String> createAccount(@RequestBody CreateAccountDTO body) {
        HttpRequest<CreateAccountDTO, Void> request = new HttpRequest<>();
        request.body = body;
        return this.handler(request);
    }

    @Override
    public HttpResponse<String> perform(
            HttpRequest<CreateAccountDTO, Void> input) {
        return null;
    }
}

