//package org.example.adds.WebSocket;
//
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.http.server.ServerHttpResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.WebSocketHandler;
//import org.springframework.web.socket.server.HandshakeInterceptor;
//
//import java.util.Map;
//
//@Component
//public class WebSocketAuthInterceptor implements HandshakeInterceptor {
//
//    @Override
//    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
//                               WebSocketHandler wsHandler, Exception exception) {
//        // Do nothing after handshake
//    }
//
//    @Override
//    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
//        if (request.getURI().getQuery() != null && request.getURI().getQuery().contains("token=")) {
//            String token = request.getURI().getQuery().split("token=")[1];
//            attributes.put("token", token);
//        }
//        return true;
//    }
//}