package org.example.adds.Auth.Dto;

import org.example.adds.Users.Users;

import java.util.UUID;

public record UpdateRole(
        UUID userId,
        Users.UserRole newRole
) {
}
