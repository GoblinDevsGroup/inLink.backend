package org.example.adds.Users;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersDto {

    @Column(nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
