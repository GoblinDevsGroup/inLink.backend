package org.example.adds.Transactions.Dto;

import lombok.Data;
import org.example.adds.Transactions.TransactionState;
import org.example.adds.Transactions.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TransactionResponse {
    private UUID id;
    private String method;
    private TransactionType type;
    private BigDecimal amount;
    private TransactionState transactionState;
    private LocalDateTime transactionTime;

    public TransactionResponse(UUID id,
                               String method,
                               TransactionType type,
                               BigDecimal amount,
                               TransactionState transactionState,
                               LocalDateTime transactionTime) {
        this.id = id;
        this.method = method;
        this.type = type;
        this.amount = amount;
        this.transactionState = transactionState;
        this.transactionTime = transactionTime;
    }
}
