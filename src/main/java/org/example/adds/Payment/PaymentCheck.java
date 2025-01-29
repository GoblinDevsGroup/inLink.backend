package org.example.adds.Payment;

import jakarta.persistence.*;
import lombok.Data;
import org.example.adds.Users.Users;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table
@Data
public class PaymentCheck implements Serializable {

    @Id
    @GeneratedValue
    private UUID id;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Users uploadedBy;

    @Column(nullable = false)
    @Lob
    private byte[] file;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private String fileType;

    @Column(nullable = false)
    private LocalDateTime uploadDate;

    @Column(nullable = false)
    private LocalDateTime updatedDate;
}
