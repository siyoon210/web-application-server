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
import java.util.Collection;
import java.util.Map;

import static util.HttpRequestUtils.parseCookies;

class UserListController implements Controller {
    private static final Controller instance = new UserListController();
    private static final Logger log = LoggerFactory.getLogger(UserListController.class);

    private UserListController() {}

    public static Controller getInstance() {
        return instance;
    }

    @Override
    public void process(Map<String, String> requestInfo, OutputStream out) throws IOException {
        boolean logined = false;
        try {
            final String cookie = requestInfo.get("Cookie");
            final Map<String, String> cookies = parseCookies(cookie);
            logined = Boolean.parseBoolean(cookies.get("logined"));
        } catch (NullPointerException e) {
            log.info("invalid cookie");
        }

        final DataOutputStream dos = new DataOutputStream(out);

        if (logined) {
            log.info("Login user");
            final Collection<User> all = DataBase.findAll();
            StringBuilder sb = new StringBuilder();
            for (User user : all) {
                sb.append(user.getName()).append(": ").append(user.getEmail()).append("\r\n");
            }

            final byte[] body = sb.toString().getBytes();

            response200Header(dos, body.length);
            responseBody(dos, body);
        } else {
            log.info("Logout user");
            final byte[] body = Files.readAllBytes(new File("./webapp" + "/login.html").toPath());

            response200Header(dos, body.length);
            responseBody(dos, body);
        }
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
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
