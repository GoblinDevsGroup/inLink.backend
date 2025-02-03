package org.example.adds.Transactions;

import lombok.AllArgsConstructor;
import org.example.adds.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transaction")
@AllArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/get/{userId}")
    public ResponseEntity<List<TransactionResponse>> getAllTransactions(@PathVariable UUID userId){
        return ResponseEntity.ok(transactionService.getAllTransactionByUserId(userId));
    }
}
