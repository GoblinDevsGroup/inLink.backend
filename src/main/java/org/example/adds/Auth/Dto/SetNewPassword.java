package org.example.adds.Auth.Dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SetNewPassword(
        @NotNull
        @Size(min = 6, message = "Phone must be valid")
        String phone,
        @NotNull
        @Size(min = 6, message = "Password must be at least 6 characters long")
        String newPassword
) {
}
