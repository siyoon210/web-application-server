package webserver.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class HttpSessions {
    private static final Map<String, HttpSession> sessions = new HashMap<>();

    public static void set(String key, HttpSession session) {
        sessions.put(key, session);
    }

    public static HttpSession get(String key) {
        HttpSession httpSession = sessions.get(key);

        if (Objects.isNull(httpSession)) {
            final String id = UUID.randomUUID().toString();
            httpSession = new HttpSession(id);
            set(id, httpSession);
        }

        return httpSession;
    }

    public static void remove(String key) {
        sessions.remove(key);
    }
}
