package org.example.adds.Contact;

import lombok.AllArgsConstructor;
import org.example.adds.Users.Users;
import org.example.adds.Users.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@AllArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final UsersService usersService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/send-to-admin")
    public ResponseEntity<Chat> sentToAdmin(@RequestBody ChatMessage message) {
        Chat response = chatService.saveMessageFromUser(message);
        Users user = usersService.findById(message.getUserId());

        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor
                .create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(user.getPhone());
        headerAccessor.setLeaveMutable(true);

//        simpMessagingTemplate.convertAndSendToUser(user.getPhone(), //user ulanadigan kanal
//                "/queue/private",
//                response,
//                headerAccessor.getMessageHeaders());

        simpMessagingTemplate.convertAndSend("/queue/message", response); //admin ulanadigan kanal
        return ResponseEntity.ok(response);
    }

    @PostMapping("/send-to-user")
    public ResponseEntity<Chat> sentToUser(@RequestBody ChatMessage message) {
        Chat response = chatService.saveMessageFromAdmin(message);
        Users user = usersService.findById(message.getUserId());

        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor
                .create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(user.getPhone());
        headerAccessor.setLeaveMutable(true);

        simpMessagingTemplate.convertAndSendToUser(user.getPhone(), //user ulanadigan kanal
                "/queue/private",
                response,
                headerAccessor.getMessageHeaders());

//        simpMessagingTemplate.convertAndSend("/queue/message", response); //admin ulanadigan kanal
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-all-chat")
    public ResponseEntity<List<ChatResponse>> getAllChat() {
        return ResponseEntity.ok(chatService.findAllByGroup());
    }

    @GetMapping("/view-one/{chatName}")
    public ResponseEntity<ChatResponse> viewChat(@PathVariable String chatName) {
        return ResponseEntity.ok(chatService.viewOneChat(chatName));
    }
}
