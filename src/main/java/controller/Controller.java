package controller;

import webserver.model.HttpRequest;
import webserver.model.HttpResponse;

import java.io.IOException;

public interface Controller {
    HttpResponse process(HttpRequest httpRequest) throws IOException, IllegalAccessException;
}
