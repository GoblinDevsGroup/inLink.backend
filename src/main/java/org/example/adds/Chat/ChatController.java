package org.example.adds.Chat;

import lombok.AllArgsConstructor;
import org.example.adds.Chat.Dto.ChatRequest;
import org.example.adds.Chat.Dto.ChatResponse;
import org.example.adds.Users.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/chat")
@AllArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/send")
    public void send(@RequestBody ChatRequest request){
        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        chatService.send(userDetails.getUsername(), request);
    }

    @GetMapping("/get-chats/{userId}")
    public ResponseEntity<List<ChatResponse>> viewChat(@PathVariable UUID userId) {
        return ResponseEntity.ok(chatService.viewOneChat(userId));
    }
}
