package controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Controller {
    void process(InputStream in, OutputStream out) throws IOException;
}
