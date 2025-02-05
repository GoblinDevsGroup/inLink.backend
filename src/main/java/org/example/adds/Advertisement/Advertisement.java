package org.example.adds.Advertisement;

import jakarta.persistence.*;
import lombok.Data;
import org.example.adds.Users.Users;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table
@Data
public class Advertisement implements Serializable {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private AdStatus status;

    @Column(nullable = false, unique = true)
    private String advLink;

    @Column(nullable = false)
    private String mainLink;

    @Column(nullable = false)
    private int VisitorNumber;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Users user;

    @Lob
    @JoinColumn
    private byte[] qrCode;

    @JoinColumn
    private LocalDateTime updatedAt;
}
