package model;

public enum MediaType {
    HTML("text/html"),
    CSS("text/css"),
    JS("text/javascript"),
    PNG("image/png"),
    ICO("image/ico"),
    WOFF("font/woff");

    private final String mediaType;

    MediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public static String getMediaType(String fileType) {
        try {
            return valueOf(fileType.toUpperCase()).mediaType;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return "unknown";
        }
    }
}
