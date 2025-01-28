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
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

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
        wallet.setUpdatedAt(LocalDateTime.now());
        //todo: transfer charged amount into Admin wallet
        walletRepo.save(wallet);

        /*logging transactions when charging is happened from a user wallet*/
        transactionService.saveTransactionForWithdrawal(wallet, linkPrice, advTitle);

        //todo: implement sms notification to a user about transaction happened
    }

    @SneakyThrows
    @Transactional
    public void chargeFromWallet(Wallet wallet, BigDecimal amount) {
        BigDecimal updatedBalance = wallet.getBalance().subtract(amount);
        wallet.setBalance(updatedBalance);
        wallet.setUpdatedAt(LocalDateTime.now());
        //todo: transfer charged amount into Admin wallet
        walletRepo.save(wallet);

        /*logging transactions when charging is happened from a user wallet*/
        transactionService.saveTransactionForWithdrawal(wallet, amount);

        //todo: implement sms notification to a user about transaction happened
    }

    @Transactional
    public Response fillUserWallet(DepositToWallet request) {
        Wallet wallet = walletRepo.findById(request.walletId())
                .orElseThrow(()->new NoSuchElementException("wallet not found"));
        BigDecimal currentBalance = wallet.getBalance();
        BigDecimal updatedBalance = currentBalance.add(request.amount());
        wallet.setBalance(updatedBalance);
        wallet.setUpdatedAt(LocalDateTime.now());
        walletRepo.save(wallet);

        transactionService.saveTransactionForDepositing(wallet, request.amount());

        //todo: implement sms notification to a user about transaction happened
        return new Response("updated user wallet", true);
    }

}
