package org.example.adds.Chat;

import lombok.AllArgsConstructor;
import org.example.adds.Chat.Dto.ChatMessage;
import org.example.adds.Chat.Dto.ChatMessageFromAdmin;
import org.example.adds.Chat.Dto.ChatResponse;
import org.example.adds.Users.Users;
import org.example.adds.Users.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/chat")
@AllArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final UsersService usersService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/send-to-admin")
    public ResponseEntity<MessageFromUser> sentToAdmin(@RequestBody ChatMessage message) {
        Chat response = chatService.saveMessageFromUser(message);
        Users user = usersService.findById(message.getUserId());

        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create();
        headerAccessor.setContentType(MimeTypeUtils.APPLICATION_JSON);
        headerAccessor.setSessionId(user.getId().toString());
        headerAccessor.setLeaveMutable(true);

        simpMessagingTemplate.convertAndSendToUser(
                chatService.adminId().toString(),
                "/queue/private",
                response);

        return ResponseEntity.ok(new MessageFromUser(response));
    }

    @PostMapping("/send-to-user")
    public ResponseEntity<Chat> sentToUser(@RequestBody ChatMessageFromAdmin message) {
        Chat response = chatService.saveMessageFromAdmin(message);
        Users user = usersService.findById(response.getUserId());

        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor
                .create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(user.getPhone());
        headerAccessor.setLeaveMutable(true);

        simpMessagingTemplate.convertAndSendToUser(
                user.getId().toString(),
                "/queue/private",
                response);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-all-chat")
    public ResponseEntity<List<ChatResponse>> getAllChat() {
        return ResponseEntity.ok(chatService.findAllByGroup());
    }

    @GetMapping("/view-one/{userId}")
    public ResponseEntity<ChatResponse> viewChat(@PathVariable UUID userId) {
        return ResponseEntity.ok(chatService.viewOneChat(userId));
    }
}
