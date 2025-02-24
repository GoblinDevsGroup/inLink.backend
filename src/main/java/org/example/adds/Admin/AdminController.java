package org.example.adds.Admin;

import lombok.AllArgsConstructor;
import org.example.adds.Admin.Dtos.WalletResponse;
import org.example.adds.Advertisement.AdvertisementService;
import org.example.adds.Advertisement.Dto.AdvResponse;
import org.example.adds.Users.Users;
import org.example.adds.Users.UsersDto;
import org.example.adds.Users.UsersService;
import org.example.adds.Transactions.Transaction;
import org.example.adds.Transactions.TransactionService;
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
    private final UsersService usersService;
    private final WalletService walletService;
    private final TransactionService transactionService;
    private final AdvertisementService advertisementService;

    @GetMapping
    public ResponseEntity<List<Users>> getAllUsers() {
        return ResponseEntity.ok(this.usersService.getAllUsers());
    }
    @GetMapping("/get-all/adv")
    public ResponseEntity<List<AdvResponse>> getAllAdvertisements() {
        return ResponseEntity.ok(this.advertisementService.getAllAdv());
    }

    @GetMapping("/getUser/transactions")
    public ResponseEntity<List<Transaction>> getAllTransactionsWithWalletId(@RequestParam(value = "id") UUID walletId) {
        return ResponseEntity.ok(this.transactionService.getAllTransactionWithWalletId(walletId));
    }

    @GetMapping("/getUser")
    public ResponseEntity<UsersDto> getUserById(@RequestParam(value = "id") UUID userId) {
       return ResponseEntity.ok(this.usersService.getUserById(userId));
    }

    @GetMapping("/all-wallets")
    public ResponseEntity<List<WalletResponse>> getAllWallets(){
        return ResponseEntity.ok(walletService.getAllWallets());
    }
}
