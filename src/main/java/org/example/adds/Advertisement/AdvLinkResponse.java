package org.example.adds.Advertisement;

import lombok.Data;

import java.util.UUID;

@Data
public class AdvLinkResponse {
    private UUID id;
    private UUID userId;
    private String title;
    private String advLink;
    private String mainLink;

    public AdvLinkResponse(UUID id, UUID userId, String title, String advLink, String mainLink) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.advLink = advLink;
        this.mainLink = mainLink;
    }
}
