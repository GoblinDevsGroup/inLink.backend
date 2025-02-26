package org.example.adds.Chat;

import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class MessageFromUser {
    private UUID id;
    private UUID userId;
    private String message;
    private LocalDateTime time;

    public MessageFromUser(Chat chat) {
        this.id = chat.getId();
        this.userId = chat.getUserId();
        this.message = chat.getMessage();
        this.time = chat.getTime();
    }
}
