package org.example.adds.Wallet;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.example.adds.Users.Users;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class WalletService {

    private final WalletRepo walletRepo;
    private final TransactionService transactionService;
    public static final BigDecimal linkPrice = BigDecimal.valueOf(1000.00);

    public boolean checkUserWallet(Users user) {
        Wallet wallet = walletRepo.findByUser(user)
                .orElseThrow(() -> new NoSuchElementException("wallet not found"));
        return wallet.getBalance().compareTo(linkPrice) >= 0;
    }

    public Wallet getWallet(Users user) {
        return walletRepo.findByUser(user).orElseThrow(() -> new NoSuchElementException("wallet not found"));
    }

    @SneakyThrows
    @Transactional
    public void chargeFromWallet(Users user, String advTitle) {
        Wallet wallet = getWallet(user);
        BigDecimal currentBalance = wallet.getBalance();
        BigDecimal updatedBalance = currentBalance.subtract(WalletService.linkPrice);
        wallet.setBalance(updatedBalance);
        walletRepo.save(wallet);

        transactionService.saveTransaction(wallet, linkPrice, advTitle);
    }
}
