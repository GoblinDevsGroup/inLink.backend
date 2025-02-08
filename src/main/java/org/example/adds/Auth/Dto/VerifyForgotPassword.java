package org.example.adds.Auth.Dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record VerifyForgotPassword(
        @NotNull
        @Size(min = 6, message = "Phone must be valid")
        String phone,
        @NotNull
        String smsCode
) {
}
