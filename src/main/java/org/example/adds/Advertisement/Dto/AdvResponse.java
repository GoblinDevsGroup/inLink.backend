package org.example.adds.Advertisement.Dto;

import lombok.Data;
import org.example.adds.Advertisement.AdStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AdvResponse {
    private UUID id;
    private String title;
    private AdStatus status;
    private String advLink;
    private String mainLink;
    private int VisitorNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}