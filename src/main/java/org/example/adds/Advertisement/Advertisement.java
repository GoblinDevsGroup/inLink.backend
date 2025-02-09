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

    @JoinColumn(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'ACTIVE'")
    @Enumerated(EnumType.STRING)
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

    public Advertisement(UUID id,
                         String title,
                         AdStatus status,
                         String advLink,
                         String mainLink,
                         int visitorNumber,
                         LocalDateTime createdAt,
                         Users user,
                         byte[] qrCode,
                         LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.advLink = advLink;
        this.mainLink = mainLink;
        VisitorNumber = visitorNumber;
        this.createdAt = createdAt;
        this.user = user;
        this.qrCode = qrCode;
        this.updatedAt = updatedAt;
    }
    public Advertisement() {
    }
}
