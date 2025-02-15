package org.example.adds.Chat.Dto;

import java.util.UUID;

public record ChatMessageFromAdmin(
        UUID adminId,
        UUID userId,
        String content

) {
}
