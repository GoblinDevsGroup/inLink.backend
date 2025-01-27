//package org.example.adds.Contact;
//
//import lombok.AllArgsConstructor;
//import org.example.adds.Users.Users;
//import org.example.adds.Users.UsersService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
//import org.springframework.messaging.simp.SimpMessageType;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.messaging.simp.annotation.SendToUser;
//import org.springframework.web.bind.annotation.*;
//
//import java.security.Principal;
//import java.time.LocalDateTime;
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/api/chat")
//@AllArgsConstructor
//public class ContactController {
//
//    private final MessageService messageService;
//    private final SimpMessagingTemplate simpMessagingTemplate;
//    private final UsersService usersService;
//
//    @PostMapping("/sent-to-admin")
//    public void sentToAdmin(@RequestBody ChatMessage message){
//        Message response = messageService.saveMessage(message);
//        Users user = usersService.findById(message.getUserId());
//
//        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor
//                .create(SimpMessageType.MESSAGE);
//        headerAccessor.setSessionId(user.getPhone());
//        headerAccessor.setLeaveMutable(true);
//
//        simpMessagingTemplate.convertAndSendToUser(user.getPhone(), //user ulanadigan kanal
//                "/queue/private",
//                response,
//                headerAccessor.getMessageHeaders());
//
//        simpMessagingTemplate.convertAndSend("/queue/message", response); //admin ulanadigan kanal
//    }
//
//    @GetMapping("/view-message")
//    public ResponseEntity<?> viewMessage(){
//        return ResponseEntity.ok(messageService.getAll());
//    }
//
//    @PostMapping("/sent-to-user")
//    public ResponseEntity<?> sendToUser(@RequestBody ChatMessage message){
//        messageService.sendTouser
//    }
//}
