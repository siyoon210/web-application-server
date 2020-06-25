package controller.model;

import java.io.OutputStream;

public interface Response {
    void write(OutputStream outputStream);
}
