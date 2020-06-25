package controller;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Map;

import static util.HttpRequestUtils.parseQueryString;

class UserCreateController implements Controller {
    private static final Controller instance = new UserCreateController();
    private static final Logger log = LoggerFactory.getLogger(UserCreateController.class);

    private UserCreateController() {}

    public static Controller getInstance() {
        return instance;
    }

    @Override
    public void process(Map<String, String> requestInfo, OutputStream out) throws IOException {
        final String path = requestInfo.get("Path");
        final Map<String, String> queryString = parseQueryString(path.split("\\?")[1]);
        User newUser = new User(queryString.get("userId"), queryString.get("password"), queryString.get("name"), queryString.get("email"));

        log.info("Create User: {}", newUser.toString());
        DataBase.addUser(newUser);

        final byte[] body = Files.readAllBytes(new File("./webapp" + "/index.html").toPath());

        final DataOutputStream dos = new DataOutputStream(out);
        response200Header(dos, 0);
        responseBody(dos, null);
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
