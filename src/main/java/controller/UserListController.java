package controller;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.model.HttpRequest;
import webserver.model.HttpResponse;
import webserver.model.HttpSession;

import java.io.IOException;
import java.util.Collection;

class UserListController extends AbstractController {
    private static final Controller instance = new UserListController();
    private static final Logger log = LoggerFactory.getLogger(UserListController.class);

    private UserListController() {}

    public static Controller getInstance() {
        return instance;
    }

    @Override
    protected HttpResponse doGet(HttpRequest request) throws IOException {
        if (isLogined(request)) {
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
        final HttpSession session = request.getSession();
        final Object user = session.get("user");
        return user != null;
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
