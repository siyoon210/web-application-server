package controller;

import webserver.model.HttpCookie;
import webserver.model.HttpRequest;
import db.DataBase;
import webserver.model.HttpResponse;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

class UserListController extends AbstractController {
    private static final Controller instance = new UserListController();
    private static final Logger log = LoggerFactory.getLogger(UserListController.class);

    private UserListController() {}

    public static Controller getInstance() {
        return instance;
    }

    @Override
    protected HttpResponse doGet(HttpRequest request) throws IOException {
        boolean logined = isLogined(request);

        if (logined) {
            log.info("Login user");
            final byte[] body = getUserListAsBytes();
            return HttpResponse.builder()
                    .status(200)
                    .header("Content-Type", "text/html;charset=utf-8")
                    .header("Content-Length", body.length)
                    .body(body)
                    .build();
        } else {
            log.info("Logout user");
            return HttpResponse.builder()
                    .status(302)
                    .redirect("/user/login.html")
                    .build();
        }
    }

    private boolean isLogined(HttpRequest request) {
        try {
            final HttpCookie cookies = request.getCookies();
            return Boolean.parseBoolean(cookies.get("logined"));
        } catch (NullPointerException e) {
            log.info("invalid cookie");
            return false;
        }
    }

    private byte[] getUserListAsBytes() {
        final Collection<User> all = DataBase.findAll();
        final StringBuilder sb = new StringBuilder();
        for (User user : all) {
            sb.append(user.getName()).append(": ").append(user.getEmail()).append("<br>");
        }
        return sb.toString().getBytes();
    }
}
