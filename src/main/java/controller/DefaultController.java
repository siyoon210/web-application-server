package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Map;

class DefaultController implements Controller {
    private static final Controller instance = new DefaultController();
    private static final Logger log = LoggerFactory.getLogger(DefaultController.class);

    private DefaultController() {
    }

    public static Controller getInstance() {
        return instance;
    }

    @Override
    public void process(Map<String, String> requestInfo, OutputStream out) throws IOException {
        String path = requestInfo.get("Path");
        if (path.equals("/")) {
            path = "/index.html";
        }

        final byte[] body = Files.readAllBytes(new File("./webapp" + path).toPath());

        final String fileType = path.substring(path.lastIndexOf(".") + 1);
        DataOutputStream dos = new DataOutputStream(out);
        response200Header(dos, body.length, fileType);
        responseBody(dos, body);
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String fileType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/" + fileType + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
