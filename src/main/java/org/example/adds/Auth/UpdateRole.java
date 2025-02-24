package org.example.adds.Auth;

import org.example.adds.Users.Users;

import java.util.UUID;

public record UpdateRole(
        UUID userId,
        Users.UserRole newRole
) {
}
