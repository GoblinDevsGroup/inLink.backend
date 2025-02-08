package org.example.adds.Advertisement.Dto;

import java.util.UUID;

public record DeleteRequest(
        UUID advId,
        UUID userId
) {
}
