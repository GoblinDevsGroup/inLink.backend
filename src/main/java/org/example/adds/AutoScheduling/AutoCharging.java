package org.example.adds.AutoScheduling;

import lombok.AllArgsConstructor;
import org.example.adds.Advertisement.AdStatus;
import org.example.adds.Advertisement.Advertisement;
import org.example.adds.Advertisement.AdvertisementRepo;
import org.example.adds.Users.Users;
import org.example.adds.Users.UsersRepo;
import org.example.adds.Wallet.Wallet;
import org.example.adds.Wallet.WalletService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
@AllArgsConstructor
@EnableScheduling
public class AutoCharging {

    private final WalletService walletService;
    private final AdvertisementRepo advertisementRepo;
    private final UsersRepo usersRepo;

    @Scheduled(cron = "0 0 0 * * ?", zone = "Asia/Tashkent")
    public void AutoChargeFromWallet() {
        List<Users> users = usersRepo.findAll();

        for (Users user : users) {
            charging(user);
        }
    }

    private void charging(Users user) {
        List<Advertisement> activeAdvList = advertisementRepo.findByUser(user)
                .stream()
                .filter(advertisement -> advertisement.getStatus() == AdStatus.ACTIVE)
                .toList();

        Wallet wallet = walletService.getWallet(user);

        if (wallet.getBalance().compareTo(
                WalletService.linkPrice.multiply(BigDecimal.valueOf(activeAdvList.size()))) >= 0) {

            /* once user balance is enough, charging is done for an amount */
            BigDecimal amount = WalletService.linkPrice.multiply(BigDecimal.valueOf(activeAdvList.size()));
            walletService.chargeFromWallet(wallet, amount);

        } else {
            /* if balance is not enough, then all active advertisements will be inactive */
            for (Advertisement adv : activeAdvList) {
                adv.setStatus(AdStatus.INACTIVE);
                adv.setUpdatedAt(LocalDateTime.now());
                advertisementRepo.save(adv);

                //todo: implement sms sending feature to notify about failing in charging amount and being inactivated
            }
        }

    }
}
