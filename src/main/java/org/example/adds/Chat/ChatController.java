package org.example.adds.Chat;

import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.example.adds.Chat.Dto.ChatRequest;
import org.example.adds.Chat.Dto.ChatResponse;
import org.example.adds.Users.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/api/chat")
@AllArgsConstructor
public class ChatController {

    private final ChatService chatService;

//    @PostMapping("/send")
//    public void send(@RequestBody ChatRequest request){
//        UserDetails userDetails = (UserDetails) SecurityContextHolder
//                .getContext()
//                .getAuthentication()
//                .getPrincipal();
//
//        chatService.send(userDetails.getUsername(), request);
//    }

    @PostMapping("/send")
    public void send(@RequestParam("receiverId") UUID receiverId,
                     @RequestParam(value = "message", required = false) String message,
                     @RequestParam(value = "file", required = false) MultipartFile file)
            throws BadRequestException {

        UserDetails sender = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        if ((file == null || file.isEmpty()) && (message == null || message.trim().isEmpty())) {
            throw new BadRequestException("Message and file cannot both be null or empty!");
        }

        chatService.send(sender.getUsername(), receiverId, message, file);
    }

    @GetMapping("/get-chats/{userId}")
    public ResponseEntity<List<ChatResponse>> viewChat(@PathVariable UUID userId) {
        return ResponseEntity.ok(chatService.viewOneChat(userId));
    }

    @PatchMapping("/edit/{messageId}")
    public void editMessage(@PathVariable UUID messageId,
                            @RequestParam("message") String message){
        chatService.editMessage(messageId, message);
    }
}
