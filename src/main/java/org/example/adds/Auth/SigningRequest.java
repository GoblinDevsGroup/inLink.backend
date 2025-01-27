package org.example.adds.Auth;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SigningRequest {
    @NotNull
    @Size(min = 6, message = "Phone must be valid")
    private String phone;
    @NotNull
    private String smsCode;
}
