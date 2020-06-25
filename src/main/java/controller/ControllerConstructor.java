package controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ControllerConstructor {
    private final static Map<String, Controller> pathAndControllers = new HashMap<>();
    static {
        pathAndControllers.put("POST /user/create", UserCreateController.getInstance());
        pathAndControllers.put("POST /user/login", UserLoginController.getInstance());
        pathAndControllers.put("GET /user/list", UserListController.getInstance());
    }

    public static Controller getController(Map<String, String> requestInfo) {
        String method = requestInfo.get("Method");
        String path = requestInfo.get("Path");

        if (hasQueryString(path)) {
            path = subStringQueryString(path);
        }

        final Controller controller = pathAndControllers.get(method + " " + path);

        if (Objects.isNull(controller)) {
            return DefaultController.getInstance();
        }

        return controller;
    }

    private static String subStringQueryString(String path) {
        return path.split("\\?")[0];
    }

    private static boolean hasQueryString(String path) {
        return path.contains("?");
    }
}
