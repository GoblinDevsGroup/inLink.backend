package org.example.adds.Transactions;

import org.example.adds.Transactions.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, UUID> {

    List<Transaction>findAllByWallet_Id(UUID walletId);
}
