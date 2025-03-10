package org.example.adds.Chat.Dto;

import lombok.Data;
import org.example.adds.Users.Users;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ChatResponse {
    private UUID id;
    private Users.UserRole userRole;
    private String message;
    private UUID fileId;
    private LocalDateTime time;

    public ChatResponse(UUID id,
                        Users.UserRole userRole,
                        String message,
                        UUID fileId,
                        LocalDateTime time) {
        this.id = id;
        this.userRole = userRole;
        this.message = message;
        this.fileId = fileId;
        this.time = time;
    }
}
