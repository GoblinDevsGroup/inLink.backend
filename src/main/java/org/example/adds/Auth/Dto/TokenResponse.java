package org.example.adds.Auth.Dto;

import lombok.Data;
import lombok.Getter;

@Data
public class TokenResponse {
    private String accessToken;
    private String fullName;

    public TokenResponse(String accessToken, String fullName) {
        this.accessToken = accessToken;
        this.fullName = fullName;
    }
}
