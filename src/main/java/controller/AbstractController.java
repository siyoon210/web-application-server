package controller;

import controller.model.HttpRequest;
import controller.model.HttpResponse;

import java.io.IOException;

public abstract class AbstractController implements Controller {
    public final HttpResponse process(HttpRequest httpRequest) throws IOException, IllegalAccessException {
        final String method = httpRequest.getMethod();

        switch (method) {
            case "GET":
                return doGet(httpRequest);
            case "POST":
                return doPost(httpRequest);
            default:
                throw new IllegalArgumentException("Illegal Http Method");
        }
    }

    protected HttpResponse doGet(HttpRequest httpRequest) throws IOException, IllegalAccessException {
        throw new IllegalAccessException("doGet() is not overridden.");
    }

    protected HttpResponse doPost(HttpRequest httpRequest) throws IOException, IllegalAccessException {
        throw new IllegalAccessException("doPost() is not overridden.");
    }
}
