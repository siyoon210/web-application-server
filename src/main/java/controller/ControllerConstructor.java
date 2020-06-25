package controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ControllerConstructor {
    private final static Map<String, Controller> pathAndControllers = new HashMap<>();
    static {
        pathAndControllers.put("/", new MainController());
        pathAndControllers.put("/index.html", new MainController());
    }

    public static Controller getController(String path) {
        final Controller controller = pathAndControllers.get(path);
        if (Objects.isNull(controller)) {
            return new DefaultController();
        }
        return controller;
    }
}
