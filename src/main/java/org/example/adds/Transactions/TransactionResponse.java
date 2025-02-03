package org.example.adds.Transactions;

import jakarta.persistence.Column;
import lombok.Data;
import org.example.adds.Wallet.Valuate;
import org.example.adds.Wallet.Wallet;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TransactionResponse {
    private UUID id;
    private TransactionType type;
    private Valuate valuate;
    private String description;
    private BigDecimal amount;
    private TransactionState transactionState;
    private LocalDateTime transactionTime;

    public TransactionResponse(UUID id,
                               TransactionType type,
                               Valuate valuate,
                               String description,
                               BigDecimal amount,
                               TransactionState transactionState,
                               LocalDateTime transactionTime) {
        this.id = id;
        this.type = type;
        this.valuate = valuate;
        this.description = description;
        this.amount = amount;
        this.transactionState = transactionState;
        this.transactionTime = transactionTime;
    }
}
