package controller;

import controller.model.HttpRequest;
import db.DataBase;
import controller.model.RedirectResponse;
import controller.model.Response;
import controller.model.StaticFileResponse;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    public Response process(HttpRequest request) {
        boolean logined = false;
        try {
            final String cookie = request.get("Cookie");
            final Map<String, String> cookies = parseCookies(cookie);
            logined = Boolean.parseBoolean(cookies.get("logined"));
        } catch (NullPointerException e) {
            log.info("invalid cookie");
        }

        if (logined) {
            log.info("Login user");
            final Collection<User> all = DataBase.findAll();
            StringBuilder sb = new StringBuilder();
            for (User user : all) {
                sb.append(user.getName()).append(": ").append(user.getEmail()).append("<br>");
            }

            final byte[] body = sb.toString().getBytes();

            return StaticFileResponse.builder()
                    .status(200)
                    .header("Content-Type", "text/html;charset=utf-8")
                    .header("Content-Length", body.length)
                    .body(body)
                    .build();
        }

        log.info("Logout user");
        return RedirectResponse.builder()
                .status(302)
                .location("/user/login.html")
                .build();
    }
}
