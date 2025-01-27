package org.example.adds.Advertisement;

import java.util.UUID;

public record UpdateStatus(
        UUID userId,
        UUID advId,
        AdStatus status
) {
}
