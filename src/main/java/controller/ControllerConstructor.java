package controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ControllerConstructor {
    private final static Map<String, Controller> pathAndControllers = new HashMap<>();
    static {
        pathAndControllers.put("GET /user/create", UserGetCreateController.getInstance());
        pathAndControllers.put("POST /user/create", UserPostCreateController.getInstance());
        pathAndControllers.put("POST /user/login", UserLoginController.getInstance());
        pathAndControllers.put("GET /user/list", UserListController.getInstance());
    }

    public static Controller getController(Map<String, String> requestInfo) {
        String method = requestInfo.get("Method");
        String path = requestInfo.get("Path");

        if (hasQueryString(path)) {
            path = path.split("\\?")[0];
        }

        final Controller controller = pathAndControllers.get(method + " " + path);

        if (Objects.isNull(controller)) {
            return DefaultController.getInstance();
        }

        return controller;
    }

    private static boolean hasQueryString(String path) {
        return path.contains("?");
    }
}
