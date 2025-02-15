//package org.example.adds.WebSocket;
//
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.http.server.ServletServerHttpRequest;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.WebSocketHandler;
//import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
//
//import java.security.Principal;
//import java.util.Map;
//
//@Component
//public class CustomHandler  extends DefaultHandshakeHandler {
//
//    @Override
//    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
//                                      Map<String, Object> attributes) {
//        if (request instanceof ServletServerHttpRequest) {
//            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
//            String token = extractTokenFromRequest(servletRequest);
//
//            if (token != null && validateToken(token)) {
//                return new WebSocketUser(getUsernameFromToken(token)); // Authenticated user
//            }
//        }
//        return null; // Reject connection if token is invalid
//    }
//
//    private String extractTokenFromRequest(ServletServerHttpRequest request) {
//        // Extract token from query parameters
//        String query = request.getServletRequest().getQueryString();
//        if (query != null && query.contains("token=")) {
//            return query.split("token=")[1].split("&")[0]; // Extract token
//        }
//        return null;
//    }
//
//    private boolean validateToken(String token) {
//        // Implement your JWT validation logic here
//        return true;
//    }
//
//    private String getUsernameFromToken(String token) {
//        // Extract username or user ID from the token
//        return "user123"; // Replace with actual decoding logic
//    }
//
//    // Custom WebSocket Principal
//    public static class WebSocketUser implements Principal {
//        private final String name;
//
//        public WebSocketUser(String name) {
//            this.name = name;
//        }
//
//        @Override
//        public String getName() {
//            return name;
//        }
//    }
//}
