package mana.mana33.controller.contracts;

import mana.mana33.controller.http.HttpRequest;
import mana.mana33.controller.http.HttpResponse;

public abstract class Controller<R, IB, IQ> {
    public abstract HttpResponse<R> perform(HttpRequest<IB, IQ> input);

    public HttpResponse<R> handler(HttpRequest<IB, IQ> input) {
        return this.perform(input);
    }
}
