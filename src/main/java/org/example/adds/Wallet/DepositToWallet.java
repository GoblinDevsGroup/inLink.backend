package org.example.adds.Wallet;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record DepositToWallet(
        @NotNull
        UUID walletId,
        @NotNull
        BigDecimal amount,

        @NotNull
        String method
) {
}
