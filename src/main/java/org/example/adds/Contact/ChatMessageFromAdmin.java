package org.example.adds.Contact;

import java.util.UUID;

public record ChatMessageFromAdmin(
        UUID adminId,
        String chatName,
        String content

) {
}
