package org.example.adds.Wallet;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class TransactionService {

    private final TransactionRepo transactionRepo;

    public void saveTransactionForWithdrawal(Wallet wallet, BigDecimal amount, String advTitle) {
        Transaction transaction = new Transaction();
        transaction.setWallet(wallet);
        transaction.setAmount(amount);
        transaction.setTransactionTime(LocalDateTime.now());
        transaction.setValuate(Valuate.USZ);
        transaction.setType(TransactionType.WITHDRAWAL);
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
        transaction.setType(TransactionType.WITHDRAWAL);
        transaction.setDescription("an amount "+amount+" UZS charged for today");
        transaction.setTransactionState(TransactionState.SUCCESS);
        transactionRepo.save(transaction);
    }

    public void saveTransactionForDepositing(Wallet wallet, BigDecimal amount) {
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setWallet(wallet);
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setValuate(Valuate.USZ);
        transaction.setDescription("an amount of "+amount+" UZS has been credited");
        transaction.setTransactionTime(LocalDateTime.now());
        transaction.setTransactionState(TransactionState.SUCCESS);
        transactionRepo.save(transaction);
    }
}
