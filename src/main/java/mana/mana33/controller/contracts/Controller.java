package mana.mana33.controller.contracts;

import mana.mana33.controller.http.HttpRequest;
import mana.mana33.controller.http.HttpResponse;

public abstract class Controller<R, I, IB> {
    public HttpResponse<R> perform(HttpRequest<I, IB> input) {
        return new HttpResponse<>();
    }
}
