package org.example.adds.Admin.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.adds.Advertisement.AdStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class AdvResponseForAdmin {
    private UUID id;
    private String title;
    private String author;
    private AdStatus status;
    private String advLink;
    private String mainLink;
    private int VisitorNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
