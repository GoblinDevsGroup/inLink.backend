package org.example.adds.Contact;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class MessageResponse {
    private UUID from;
    private UUID to;
    private String message;
    private LocalDateTime time;

    public MessageResponse(UUID from, UUID to, String message, LocalDateTime time) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.time = time;
    }
}
