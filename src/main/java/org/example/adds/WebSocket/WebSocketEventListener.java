package org.example.adds.WebSocket;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.UUID;

@Service
@AllArgsConstructor
public class WebSocketEventListener {

    @Autowired
    private UserSessionService userSessionService;

    /* checking user connection to socket */
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        UUID userId = UUID.fromString(sha.getUser().getName());
        userSessionService.addSession(userId, sha.getSessionId());
    }

    /* checking user disconnection from socket */
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        UUID userId = UUID.fromString(sha.getUser().getName());
        userSessionService.removeSession(userId, sha.getSessionId());
    }
}
