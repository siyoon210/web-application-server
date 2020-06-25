package webserver;

import controller.Controller;
import controller.ControllerConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
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
            Map<String, String> requestInfo = getRequestInfoFrom(in);
            log.debug("Method: {}, Path: {}", requestInfo.get("Method"), requestInfo.get("Path"));

            Controller c = ControllerConstructor.getController(requestInfo);
            c.process(requestInfo, out);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
