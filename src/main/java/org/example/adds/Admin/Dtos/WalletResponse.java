package org.example.adds.Admin.Dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class WalletResponse {
    private String balanceUuId;
    private UUID userId;
    private BigDecimal balance;
    private String daysLeft;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public WalletResponse(String balanceUuId,
                          UUID userId,
                          BigDecimal balance,
                          String daysLeft,
                          LocalDateTime createdAt,
                          LocalDateTime updatedAt) {
        this.balanceUuId = balanceUuId;
        this.userId = userId;
        this.balance = balance;
        this.daysLeft = daysLeft;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
