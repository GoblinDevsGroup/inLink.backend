package org.example.adds.DraftUser;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table
@Data
public class DraftUsers implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column
    private String password;

    @Column
    private String smsCode;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @JoinColumn
    private LocalDateTime expiresAt;
}
