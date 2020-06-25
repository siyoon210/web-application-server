package controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ControllerConstructor {
    private final static Map<String, Controller> pathAndControllers = new HashMap<>();
    static {
        pathAndControllers.put("/", MainController.getInstance());
        pathAndControllers.put("/user/create", UserCreateController.getInstance());
    }

    public static Controller getController(String path) {
        if (path.contains("?")) {
            path = path.split("\\?")[0];
        }

        final Controller controller = pathAndControllers.get(path);

        if (Objects.isNull(controller)) {
            return DefaultController.getInstance();
        }

        return controller;
    }
}
