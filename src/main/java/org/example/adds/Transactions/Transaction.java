package org.example.adds.Transactions;

import jakarta.persistence.*;
import lombok.Data;
import org.example.adds.Wallet.Valuate;
import org.example.adds.Wallet.Wallet;

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
    @ManyToOne(fetch = FetchType.LAZY)
    private Wallet wallet;

    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false)
    private Valuate valuate;

    @JoinColumn
    private String method;

    @JoinColumn
    private String description;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private TransactionState transactionState;

    @Column(nullable = false)
    private LocalDateTime transactionTime;
}
