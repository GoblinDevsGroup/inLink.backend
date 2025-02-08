package org.example.adds.Advertisement.Dto;

import lombok.Data;

import java.util.UUID;

@Data
public class AdvDeleteView {
    private UUID id;
    private String title;
    private String advLink;
    private String mainLink;

    public AdvDeleteView(UUID id, String title, String advLink, String mainLink) {
        this.id = id;
        this.title = title;
        this.advLink = advLink;
        this.mainLink = mainLink;
    }
}
