package org.example.adds.Auth;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;


@Data
public class SignUpRequest {
    @NotBlank(message = "Full name is required")
    private String fullName;
    @NotBlank(message = "Company name is required")
    private String companyName;
    @NotBlank(message = "Phone is required")
    @Size(min = 6,  message = "Phone must be valid")
    private String phone;

    @NotBlank(message = "Password is required")
    @Size(min = 6,  message = "Password must be at least 6 characters long")
    private String password;
}
