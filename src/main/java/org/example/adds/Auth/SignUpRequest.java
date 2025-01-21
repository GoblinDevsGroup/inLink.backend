package org.example.adds.Auth;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;


@Data
public class SignUpRequest {
    @NotBlank(message = "Full name is required")
    private String fullName;
    @NotBlank(message = "Company name is required")
    private String companyName;
    @NotBlank(message = "Phone is required")
    private String phone;
    @NotBlank(message = "Password is required")
    private String password;
}
