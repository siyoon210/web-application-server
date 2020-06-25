package controller;

import controller.model.Response;

import java.io.IOException;
import java.util.Map;

public interface Controller {
    Response process(Map<String, String> requestInfo) throws IOException;
}
