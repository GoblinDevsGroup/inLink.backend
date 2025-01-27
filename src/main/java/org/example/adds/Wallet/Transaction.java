package org.example.adds.Wallet;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table
@Data
public class Transaction implements Serializable {

    @Id
    @GeneratedValue
    private UUID id;

    @JoinColumn(nullable = false)
    @ManyToOne
    private Wallet wallet;

    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false)
    private Valuate valuate;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDateTime transactionTime;
}
