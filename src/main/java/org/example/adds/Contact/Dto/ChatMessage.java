package org.example.adds.Contact.Dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ChatMessage {
    private UUID userId;
    private String content;
}
