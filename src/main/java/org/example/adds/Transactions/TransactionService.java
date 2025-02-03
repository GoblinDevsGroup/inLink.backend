package org.example.adds.Transactions;

import lombok.AllArgsConstructor;
import org.example.adds.Wallet.Valuate;
import org.example.adds.Wallet.Wallet;
import org.example.adds.Wallet.WalletRepo;
import org.example.adds.Wallet.WalletService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TransactionService {

    private final TransactionRepo transactionRepo;
    private final WalletRepo walletRepo;

    public void saveTransactionForWithdrawal(Wallet wallet, BigDecimal amount, String advTitle) {
        Transaction transaction = new Transaction();
        transaction.setWallet(wallet);
        transaction.setAmount(amount);
        transaction.setTransactionTime(LocalDateTime.now());
        transaction.setValuate(Valuate.USZ);
        transaction.setType(TransactionType.EXPENSE);
        transaction.setDescription("an amount "+amount+" UZS charged for " + advTitle);
        transaction.setTransactionState(TransactionState.SUCCESS);
        transactionRepo.save(transaction);
    }

    public void saveTransactionForWithdrawal(Wallet wallet, BigDecimal amount) {
        Transaction transaction = new Transaction();
        transaction.setWallet(wallet);
        transaction.setAmount(amount);
        transaction.setTransactionTime(LocalDateTime.now());
        transaction.setValuate(Valuate.USZ);
        transaction.setType(TransactionType.EXPENSE);
        transaction.setDescription("an amount "+amount+" UZS charged for today");
        transaction.setTransactionState(TransactionState.SUCCESS);
        transactionRepo.save(transaction);
    }

    public void saveTransactionForDepositing(Wallet wallet, BigDecimal amount) {
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setWallet(wallet);
        transaction.setType(TransactionType.INCOME);
        transaction.setValuate(Valuate.USZ);
        transaction.setDescription("an amount of "+amount+" UZS has been credited");
        transaction.setTransactionTime(LocalDateTime.now());
        transaction.setTransactionState(TransactionState.SUCCESS);
        transactionRepo.save(transaction);
    }
    public List<Transaction> getAllTransactionWithWalletId(UUID walletId) {
        return this.transactionRepo.findAllByWallet_Id(walletId);
    }

    public List<TransactionResponse> getAllTransactionByUserId(UUID userId) {
        Wallet wallet = walletRepo.findByUser_Id(userId)
                .orElseThrow(()->new NoSuchElementException("wallet not found"));

        List<Transaction> transactions = transactionRepo.findAllByWallet_Id(wallet.getId());

        String method = "";
        return transactions
                .stream()
                .map(transaction -> new TransactionResponse(
                        transaction.getId(),
                        method,
                        transaction.getType(),
                        transaction.getAmount(),
                        transaction.getTransactionState(),
                        transaction.getTransactionTime()
                ))
                .collect(Collectors.toList());
    }
}
