package org.example.adds.Contact;

import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
@AllArgsConstructor
public class ContactController {

    @MessageMapping("/send-message") // Maps to /app/send-message
    @SendToUser("/queue/messages")  // Sends messages to individual users
    public ChatMessage sendMessage(@Payload ChatMessage message, Principal user) {
        // Process the message (e.g., save to database, log, etc.)
        message.setTimestamp(LocalDateTime.now());
        return message; // Send back the processed message
    }
}
