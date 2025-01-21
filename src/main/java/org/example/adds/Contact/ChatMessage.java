package org.example.adds.Contact;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ChatMessage {
    private UUID senderId;
    private UUID recipientId;
    private String content;
    private LocalDateTime timestamp;
}
