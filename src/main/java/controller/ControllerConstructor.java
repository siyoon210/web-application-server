package controller;

import webserver.model.HttpRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ControllerConstructor {
    private final static Map<String, Controller> pathAndControllers = new HashMap<>();

    private ControllerConstructor() {
    }

    static {
        pathAndControllers.put("/user/create", UserCreateController.getInstance());
        pathAndControllers.put("/user/login", UserLoginController.getInstance());
        pathAndControllers.put("/user/list", UserListController.getInstance());
    }

    public static Controller getOf(HttpRequest request) {
        String path = request.getPath();
        if (hasQueryString(path)) {
            path = subStringQueryString(path);
        }

        final Controller controller = pathAndControllers.get(path);
        return Objects.isNull(controller) ? DefaultController.getInstance() : controller;
    }

    private static String subStringQueryString(String path) {
        return path.split("\\?")[0];
    }

    private static boolean hasQueryString(String path) {
        return path.contains("?");
    }
}
