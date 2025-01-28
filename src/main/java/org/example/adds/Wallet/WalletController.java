package org.example.adds.Wallet;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.adds.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wallet")
@AllArgsConstructor
public class WalletController {

    private final WalletService walletService;

    //todo: api for sending image of transaction

    @PatchMapping("/deposit")
    public ResponseEntity<Response> depositToUserWallet(@Valid @RequestBody DepositToWallet request){
        return ResponseEntity.ok(walletService.fillUserWallet(request));
    }

}
