package controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public interface Controller {
    void process(Map<String, String> requestInfo, OutputStream out) throws IOException;
}
