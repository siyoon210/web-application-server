package controller;

import webserver.model.HttpRequest;
import db.DataBase;
import webserver.model.HttpResponse;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

class UserCreateController extends AbstractController {
    private static final Controller instance = new UserCreateController();
    private static final Logger log = LoggerFactory.getLogger(UserCreateController.class);

    private UserCreateController() {}

    public static Controller getInstance() {
        return instance;
    }

    @Override
    protected HttpResponse doPost(HttpRequest request) {
        final Map<String, String> content = request.getParsedBody();
        final User newUser = new User(content);

        log.info("Create User: {}", newUser);
        DataBase.addUser(newUser);

        return HttpResponse.builder()
                .status(302)
                .redirect("/")
                .build();
    }
}
