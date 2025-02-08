package org.example.adds.Advertisement.Dto;

import org.example.adds.Advertisement.AdStatus;

import java.util.UUID;

public record UpdateStatus(
        UUID userId,
        UUID advId,
        AdStatus status
) {
}
