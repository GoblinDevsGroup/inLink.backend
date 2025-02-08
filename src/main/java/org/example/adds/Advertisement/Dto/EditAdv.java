package org.example.adds.Advertisement.Dto;

import java.util.UUID;

public record EditAdv(
        UUID advId,
        UUID userId,
        String title
) {
}
