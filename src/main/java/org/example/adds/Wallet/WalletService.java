package org.example.adds.Wallet;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.example.adds.Response;
import org.example.adds.Users.Users;
import org.example.adds.Users.UsersRepo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
        //todo: transfer charged amount into Admin wallet
        walletRepo.save(wallet);

        /*logging transactions when charging is happened from a user wallet*/
        transactionService.saveTransactionForWithdrawal(wallet, linkPrice, advTitle);
    }

    public Response fillUserWallet(DepositToWallet request) {
        Wallet wallet = walletRepo.findById(request.walletId())
                .orElseThrow(()->new NoSuchElementException("wallet not found"));
        BigDecimal currentBalance = wallet.getBalance();
        BigDecimal updatedBalance = currentBalance.subtract(request.amount());
        wallet.setBalance(updatedBalance);
        wallet.setUpdatedAt(LocalDateTime.now());
        walletRepo.save(wallet);

        transactionService.saveTransactionForDepositing(wallet, request.amount());
        return new Response("updated user wallet", true);
    }
}
