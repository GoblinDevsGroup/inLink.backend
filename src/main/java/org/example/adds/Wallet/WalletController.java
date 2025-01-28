package org.example.adds.Wallet;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.adds.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

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

    @PostMapping("/img/{userId}")
    public ResponseEntity<Response> sendPaymentCheck(@RequestParam("file") MultipartFile file,
                                                   @PathVariable UUID userId){
        return ResponseEntity.ok(walletService.sendPaymentCheck(userId,file));
    }

}
