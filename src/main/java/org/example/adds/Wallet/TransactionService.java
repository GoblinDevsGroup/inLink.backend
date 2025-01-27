package org.example.adds.Wallet;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class TransactionService {

    private final TransactionRepo transactionRepo;

    public void saveTransaction(Wallet wallet, BigDecimal amount, String advTitle) {
        Transaction transaction = new Transaction();
        transaction.setWallet(wallet);
        transaction.setAmount(amount);
        transaction.setTransactionTime(LocalDateTime.now());
        transaction.setValuate(Valuate.USZ);
        transaction.setType(TransactionType.WITHDRAWAL);
        transaction.setDescription("amount withdrawal for " + advTitle);
        transaction.setTransactionState(TransactionState.SUCCESS);
        transactionRepo.save(transaction);
    }
}
