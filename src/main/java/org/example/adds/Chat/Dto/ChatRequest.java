package org.example.adds.Chat.Dto;

import java.util.UUID;

public record ChatRequest(
        UUID receiverId,
        String message
) {
}
