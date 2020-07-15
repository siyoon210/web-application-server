package controller;

import webserver.model.HttpRequest;
import webserver.model.HttpResponse;

import java.io.IOException;

class DefaultController extends AbstractController {
    private static final Controller instance = new DefaultController();

    private DefaultController() {
    }

    public static Controller getInstance() {
        return instance;
    }

    @Override
    protected HttpResponse doGet(HttpRequest request) throws IOException {
        final String path = getPath(request);

        return HttpResponse.builder()
                .status(200)
                .forward(path)
                .build();
    }

    private String getPath(HttpRequest request) {
        final String path = request.getPath();
        if (path.equals("/")) {
            return "/index.html";
        }
        return path;
    }
}
