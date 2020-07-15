package controller;

import webserver.model.HttpRequest;
import webserver.model.HttpResponse;
import model.HttpMethod;

import java.io.IOException;

public abstract class AbstractController implements Controller {
    public final HttpResponse process(HttpRequest httpRequest) throws IOException, IllegalAccessException {
        final HttpMethod method = HttpMethod.valueOf(httpRequest.getMethod());

        switch (method) {
            case GET:
                return doGet(httpRequest);
            case POST:
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
