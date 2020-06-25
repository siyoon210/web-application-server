package webserver;

import controller.Controller;
import controller.ControllerConstructor;
import model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;

import static util.HttpRequestUtils.getRequestInfoFrom;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream()) {
            final Map<String, String> requestInfo = getRequestInfoFrom(in);
            final Controller c = ControllerConstructor.getController(requestInfo);
            final Response response = c.process(requestInfo);
            response.write(out);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
