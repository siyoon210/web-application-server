package controller;

import controller.model.Response;
import controller.model.StaticFileResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

class DefaultController implements Controller {
    private static final Controller instance = new DefaultController();
    private static final Logger log = LoggerFactory.getLogger(DefaultController.class);

    private DefaultController() {
    }

    public static Controller getInstance() {
        return instance;
    }

    @Override
    public Response process(Map<String, String> requestInfo) throws IOException {
        final String path = getPath(requestInfo);
        final String mediaType = getMediaType(requestInfo);

        final byte[] body = Files.readAllBytes(new File("./webapp" + path).toPath());

        return StaticFileResponse.builder()
                .status(200)
                .header("Content-Type", mediaType + ";charset=utf-8")
                .header("Content-Length", body.length)
                .body(body)
                .build();
    }

    private String getMediaType(Map<String, String> requestInfo) {
        final String path = requestInfo.get("Path");
        final String fileType = path.substring(path.lastIndexOf(".") + 1);

        switch (fileType) {
            case "html":
                return "text/html";
            case "css":
                return "text/css";
            case "js":
                return "text/javascript";
            case "png":
                return "image/png";
            case "ico":
                return "image/ico";
            case "woff":
                return "font/woff";
            default:
                log.debug("Can't find proper media-type: {}", fileType);
                return "unknown";
        }
    }

    private String getPath(Map<String, String> requestInfo) {
        String path = requestInfo.get("Path");
        if (path.equals("/")) {
            return  "/index.html";
        }
        return path;
    }
}
