package org.example.adds.Advertisement.Dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdvRequest {
    private String title;
    private String advLink;
    private String mainLink;
    private LocalDateTime expiresAt;
    private Long userId;
}
