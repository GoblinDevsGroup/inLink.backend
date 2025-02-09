package org.example.adds.Users;


import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table
@Data
public class Users implements Serializable {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public enum UserRole{
        ROLE_USER,
        ROLE_ADMIN
    }

    public Users(UUID id,
                 String fullName,
                 String companyName,
                 String phone,
                 String password,
                 UserRole userRole,
                 LocalDateTime createdAt,
                 LocalDateTime updatedAt) {
        this.id = id;
        this.fullName = fullName;
        this.companyName = companyName;
        this.phone = phone;
        this.password = password;
        this.userRole = userRole;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Users() {
    }
}
