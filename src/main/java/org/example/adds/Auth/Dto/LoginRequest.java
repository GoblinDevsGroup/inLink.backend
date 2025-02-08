package org.example.adds.Auth.Dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {
    @NotNull
    @Size(min = 6)
    private String phone;
    @NotNull
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;
}
