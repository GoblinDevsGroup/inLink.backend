package org.example.adds.Advertisement.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.adds.Advertisement.AdStatus;

import java.util.UUID;

@Data
@AllArgsConstructor
public class AdvEditResponse {
    private UUID id;
    private String author;
    private AdStatus status;
    private String advLink;
    private String mainLink;
}
