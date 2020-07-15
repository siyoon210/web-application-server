package webserver.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HttpSessions {
    private static final Map<String, HttpSession> sessions = new HashMap<>();

    public static void set(String key, HttpSession session) {
        sessions.put(key, session);
    }

    public static HttpSession get(String sessionId) {
        HttpSession httpSession = sessions.get(sessionId);

        if (Objects.isNull(httpSession)) {
            httpSession = new HttpSession(sessionId);
            set(sessionId, httpSession);
        }

        return httpSession;
    }

    public static void remove(String key) {
        sessions.remove(key);
    }
}
