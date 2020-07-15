package webserver.model;

import java.util.Map;

import static util.HttpRequestUtils.parseCookies;

public class HttpCookie {
    private final Map<String, String> cookies;

    public HttpCookie(String cookieAsString) {
        this.cookies = parseCookies(cookieAsString);
    }

    public String get(String key) {
        return cookies.get(key);
    }
}
