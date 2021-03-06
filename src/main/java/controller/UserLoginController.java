package controller;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.model.HttpRequest;
import webserver.model.HttpResponse;
import webserver.model.HttpSession;

import java.io.IOException;
import java.util.Map;

class UserLoginController extends AbstractController {
    private static final Controller instance = new UserLoginController();
    private static final Logger log = LoggerFactory.getLogger(UserLoginController.class);

    private UserLoginController() {
    }

    public static Controller getInstance() {
        return instance;
    }

    @Override
    protected HttpResponse doPost(HttpRequest request) throws IOException {
        final Map<String, String> content = request.getParsedBody();
        final HttpSession session = request.getSession();
        final User user = DataBase.findUserById(content.get("userId"));

        if (isLoginSuccess(content, user)) {
            log.debug("Login success: {}", user.getName());
            session.set("user", user);
            return HttpResponse.builder()
                    .status(302)
                    .redirect("/")
                    .cookie("logined", true)
                    .build();
        } else {
            log.debug("Login fail: {}", user == null ? "[No user]" : user.getName());
            return HttpResponse.builder()
                    .status(302)
                    .redirect("/user/login_failed.html")
                    .cookie("logined", false)
                    .build();
        }
    }

    private boolean isLoginSuccess(Map<String, String> content, User user) {
        return user != null && user.getPassword().equals(content.get("password"));
    }
}
