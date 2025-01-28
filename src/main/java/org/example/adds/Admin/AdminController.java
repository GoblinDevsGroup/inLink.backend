package org.example.adds.Admin;

import lombok.AllArgsConstructor;
import org.example.adds.Response;
import org.example.adds.Users.Users;
import org.example.adds.Users.UsersDto;
import org.example.adds.Users.UsersService;
import org.example.adds.Wallet.Transaction;
import org.example.adds.Wallet.TransactionService;
import org.example.adds.Wallet.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/admin")
@AllArgsConstructor
public class AdminController {
   // private final AdminService adminService;
    private final UsersService usersService;
   // private final WalletService walletService;
    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<Users>> getAllUsers() {
        return ResponseEntity.ok(this.usersService.getAllUsers());
    }

    @GetMapping("/getUser/transactions")
    public ResponseEntity<List<Transaction>> getAllTransactionsWithWalletId(@RequestParam(value = "id") UUID walletId) {
        return ResponseEntity.ok(this.transactionService.getAllTransactionWithWalletId(walletId));
    }
    @GetMapping("/getUser")
    public ResponseEntity<UsersDto> getUserById(@RequestParam(value = "id") UUID userId) {
       return ResponseEntity.ok(this.usersService.getUserById(userId));
    }


}
