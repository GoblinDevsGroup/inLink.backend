package org.example.adds.WebSocket;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@AllArgsConstructor
public class UserSessionService {

    private final Map<UUID, String> userSessions = new ConcurrentHashMap<>();

    public void addSession(UUID userId, String sessionId) {
        userSessions.put(userId, sessionId);
    }

    public void removeSession(UUID userId, String sessionId) {
        userSessions.remove(userId, sessionId);
    }

    public String getSessionId(UUID userId) {
        return userSessions.get(userId);
    }
}
