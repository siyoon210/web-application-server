package controller;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Map;

import static util.HttpRequestUtils.parseQueryString;

class UserPostCreateController implements Controller {
    private static final Controller instance = new UserPostCreateController();
    private static final Logger log = LoggerFactory.getLogger(UserPostCreateController.class);

    private UserPostCreateController() {}

    public static Controller getInstance() {
        return instance;
    }

    @Override
    public void process(Map<String, String> requestInfo, OutputStream out) throws IOException {
        final Map<String, String> content = parseQueryString(requestInfo.get("body"));
        User newUser = new User(content.get("userId"), content.get("password"), content.get("name"), content.get("email"));

        log.info("Create User: {}", newUser.toString());
        DataBase.addUser(newUser);

        final byte[] body = Files.readAllBytes(new File("./webapp" + "/index.html").toPath());

        final DataOutputStream dos = new DataOutputStream(out);
        response200Header(dos, body.length);
        responseBody(dos, body);
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, 0);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
