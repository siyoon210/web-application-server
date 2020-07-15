package webserver.model;

import java.util.HashMap;
import java.util.Map;

public class HttpSession {
    public static final String SESSION_ID_KEY = "JSESSIONID";
    private final Map<String, Object> value;
    private final String id;

    public HttpSession(String id) {
        this.value = new HashMap<>();
        this.id = id;
    }

    public void set(String key, Object value) {
        this.value.put(key, value);
    }

    public Object get(String key) {
        return value.get(key);
    }

    public void remove(String key) {
        value.remove(key);
    }
}
