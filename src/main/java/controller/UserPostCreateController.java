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

        final DataOutputStream dos = new DataOutputStream(out);
        response302Header(dos, "/");
    }

    private void response302Header(DataOutputStream dos, String url) {
        try {
            dos.writeBytes("HTTP/1.1 302 Redirect \r\n");
            dos.writeBytes("Location: " + url + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
