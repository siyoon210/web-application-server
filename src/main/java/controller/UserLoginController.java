package controller;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import static util.HttpRequestUtils.parseQueryString;

class UserLoginController implements Controller {
    private static final Controller instance = new UserLoginController();
    private static final Logger log = LoggerFactory.getLogger(UserLoginController.class);

    private UserLoginController() {}

    public static Controller getInstance() {
        return instance;
    }

    @Override
    public void process(Map<String, String> requestInfo, OutputStream out) throws IOException {
        final Map<String, String> content = parseQueryString(requestInfo.get("body"));
        final User user = DataBase.findUserById(content.get("userId"));

        final DataOutputStream dos = new DataOutputStream(out);

        if (user == null) {
            log.info("Login fail no user");
            response302Header(dos, "/user/login_failed.html", false);
            return;
        }

        if (user.getPassword().equals(content.get("password"))) {
            log.info("Login success: {}", user.getName());
            response302Header(dos, "/", true);
        } else {
            log.info("Login fail: {}", user.getName());
            response302Header(dos, "/user/login_failed.html", false);
        }
    }

    private void response302Header(DataOutputStream dos, String url, boolean logined) {
        try {
            dos.writeBytes("HTTP/1.1 302 Redirect \r\n");
            dos.writeBytes("Location: " + url + "\r\n");
            dos.writeBytes("Set-Cookie: logined=" + logined + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
