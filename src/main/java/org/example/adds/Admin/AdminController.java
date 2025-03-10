package org.example.adds.Admin;

import lombok.AllArgsConstructor;
import org.example.adds.Admin.Dtos.AdvResponseForAdmin;
import org.example.adds.Admin.Dtos.EditUser;
import org.example.adds.Admin.Dtos.WalletResponse;
import org.example.adds.Advertisement.AdvertisementService;
import org.example.adds.Advertisement.Dto.AdvResponse;
import org.example.adds.Response;
import org.example.adds.Users.Users;
import org.example.adds.Users.UsersDto;
import org.example.adds.Users.UsersService;
import org.example.adds.Transactions.Transaction;
import org.example.adds.Transactions.TransactionService;
import org.example.adds.Wallet.WalletService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Page<AdvResponseForAdmin>> getAllAdvertisements(
            @RequestParam(value = "searchText", required = false) String searchText,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(advertisementService.getAllByWithSearchingAndPageable(searchText, pageable));
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

    @GetMapping("/get-all/users")
    public ResponseEntity<Page<UsersDto>> getAllUsersWithPageable(
            @RequestParam(value = "searchText", required = false) String searchText,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        return ResponseEntity.ok(usersService.findBySearchingTextAndPageable(searchText, pageable));
    }

    @DeleteMapping("/delete/user/{userId}")
    public ResponseEntity<Response> deleteUser(@PathVariable UUID userId){
        usersService.deleteUser(userId);
        return ResponseEntity.ok(new Response("deleted", true));
    }

    @PatchMapping("/edit-user/{userId}")
    public ResponseEntity<Response> editUser(@PathVariable UUID userId,
                                             @RequestBody EditUser request){
        usersService.editUser(userId, request);
        return ResponseEntity.ok(new Response("user updated", true));
    }

//    @PatchMapping("/edit-adv/{advId}")
//    public ResponseEntity<Response> editAdv(@PathVariable UUID advId,
//                                            @RequestBody ){
//        advertisementService.editAdvByAdmin(advId)
//        return ResponseEntity.ok(new Response("updated", true));
//    }
}
