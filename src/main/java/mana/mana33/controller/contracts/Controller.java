package mana.mana33.controller.contracts;

import mana.mana33.controller.http.HttpRequest;
import mana.mana33.controller.http.HttpResponse;
import mana.mana33.features.contracts.Validate;

public abstract class Controller<R, IB, IQ> {
    public abstract HttpResponse<R> perform(HttpRequest<IB, IQ> input);

    public Validate<IB> getValidators() {
        return input -> {};
    }

    public HttpResponse<R> handler(HttpRequest<IB, IQ> input) {
        final Validate<IB> validator = this.getValidators();
        validator.validate(input.body);
        return this.perform(input);
    }
}
