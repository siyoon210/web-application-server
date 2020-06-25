package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import controller.Controller;
import controller.ControllerConstructor;
import controller.MainController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            Map<String, String> requestInfo = new HashMap<>();
            String line;
            while (!"".equals(line = bufferedReader.readLine())) {
                if (Objects.isNull(line)) {
                    break;
                }

                if (requestInfo.isEmpty()) {
                    final String[] s = line.split(" ");
                    requestInfo.put("Method", s[0]);
                    requestInfo.put("Path", s[1]);
                    requestInfo.put("Version", s[2]);
                    continue;
                }

                final int i = line.indexOf(":");
                requestInfo.put(line.substring(0, i), line.substring(i + 2));
            }

            log.debug("Method: {}, Path: {}", requestInfo.get("Method"), requestInfo.get("Path"));

            Controller c = ControllerConstructor.getController(requestInfo.get("Path"));
            c.process(requestInfo, out);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
