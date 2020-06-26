package controller;

import controller.model.HttpRequest;
import db.DataBase;
import controller.model.RedirectResponse;
import controller.model.Response;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static util.HttpRequestUtils.parseQueryString;

class UserLoginController implements Controller {
    private static final Controller instance = new UserLoginController();
    private static final Logger log = LoggerFactory.getLogger(UserLoginController.class);

    private UserLoginController() {
    }

    public static Controller getInstance() {
        return instance;
    }

    @Override
    public Response process(HttpRequest request) {
        final Map<String, String> content = parseQueryString(request.get("body"));
        final User user = DataBase.findUserById(content.get("userId"));

        if (isLoginFailed(content, user)) {
            log.debug("Login fail: {}", user == null ? "[No user]" : user.getName());
            return RedirectResponse.builder()
                    .status(302)
                    .location("/user/login_failed.html")
                    .cookie("logined", false)
                    .build();
        }

        log.debug("Login success: {}", user.getName());
        return RedirectResponse.builder()
                .status(302)
                .location("/")
                .cookie("logined", true)
                .build();
    }

    private boolean isLoginFailed(Map<String, String> content, User user) {
        return user == null || !user.getPassword().equals(content.get("password"));
    }
}
