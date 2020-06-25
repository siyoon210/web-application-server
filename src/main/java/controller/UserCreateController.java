package controller;

import db.DataBase;
import model.RedirectResponse;
import model.Response;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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
    public Response process(Map<String, String> requestInfo) {
        final Map<String, String> content = parseQueryString(requestInfo.get("body"));
        User newUser = new User(content);

        log.info("Create User: {}", newUser.toString());
        DataBase.addUser(newUser);

        return RedirectResponse.builder()
                .status(302)
                .location("/")
                .build();
    }
}
