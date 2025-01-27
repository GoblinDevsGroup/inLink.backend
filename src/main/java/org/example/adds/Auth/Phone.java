package org.example.adds.Auth;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record Phone(
        @NotNull
        @Size(min = 6, message = "Phone must be valid")
        String phone
) {
}
