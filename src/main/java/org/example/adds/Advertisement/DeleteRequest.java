package org.example.adds.Advertisement;

import java.util.UUID;

public record DeleteRequest(
        UUID advId,
        UUID userId
) {
}
