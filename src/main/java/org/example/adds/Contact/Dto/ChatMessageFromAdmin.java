package org.example.adds.Contact.Dto;

import java.util.UUID;

public record ChatMessageFromAdmin(
        UUID adminId,
        UUID userId,
        String content

) {
}
