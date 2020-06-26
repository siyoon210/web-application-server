package controller;

import controller.model.HttpRequest;
import controller.model.Response;

import java.io.IOException;

public interface Controller {
    Response process(HttpRequest httpRequest) throws IOException;
}
