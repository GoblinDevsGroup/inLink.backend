package org.example.adds.Admin.Dtos;

import org.example.adds.Advertisement.AdStatus;

import java.util.UUID;

public record EditAdvRequest(
        UUID advId,
        String title,
        AdStatus status
        ) {
}
