package org.example.adds.Chat.Dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class MessageResponse {
    private UUID admin;
    private UUID user;
    private String message;
    private LocalDateTime time;

    public MessageResponse(UUID admin, UUID user, String message, LocalDateTime time) {
        this.admin = admin;
        this.user = user;
        this.message = message;
        this.time = time;
    }
}
