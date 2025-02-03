package org.example.adds.Wallet;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.example.adds.Notification.NotificationService;
import org.example.adds.Payment.PaymentCheck;
import org.example.adds.Payment.PaymentCheckRepo;
import org.example.adds.Response;
import org.example.adds.Transactions.TransactionService;
import org.example.adds.Users.Users;
import org.example.adds.Users.UsersRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@AllArgsConstructor
public class WalletService {

    private final WalletRepo walletRepo;
    private final TransactionService transactionService;
    private final UsersRepo usersRepo;
    private final PaymentCheckRepo paymentCheckRepo;
    private final NotificationService notificationService;

    public static final BigDecimal linkPrice = BigDecimal.valueOf(1000.00);

    public boolean checkUserWallet(Users user) {
        Wallet wallet = walletRepo.findByUser(user)
                .orElseThrow(() -> new NoSuchElementException("wallet not found"));
        return wallet.getBalance().compareTo(linkPrice) >= 0;
    }

    public Wallet getWallet(Users user) {
        return walletRepo.findByUser(user).orElseThrow(() -> new NoSuchElementException("wallet not found"));
    }

    @SneakyThrows
    @Transactional
    public void chargeFromWallet(Users user, String advTitle) {
        Wallet wallet = getWallet(user);
        BigDecimal currentBalance = wallet.getBalance();
        BigDecimal updatedBalance = currentBalance.subtract(WalletService.linkPrice);
        wallet.setBalance(updatedBalance);
        wallet.setUpdatedAt(LocalDateTime.now());
        //todo: transfer charged amount into Admin wallet
        walletRepo.save(wallet);

        /*logging transactions when charging is happened from a user wallet*/
        transactionService.saveTransactionForWithdrawal(wallet, linkPrice, advTitle);

        /* notification to a user about transaction happened */
        notificationService.sendNotificationToUser(user.getId(),
                linkPrice + " UZS has been charged from your wallet for " + advTitle);
    }

    @SneakyThrows
    @Transactional
    public void chargeFromWallet(Wallet wallet, BigDecimal amount) {
        BigDecimal updatedBalance = wallet.getBalance().subtract(amount);
        wallet.setBalance(updatedBalance);
        wallet.setUpdatedAt(LocalDateTime.now());
        //todo: transfer charged amount into Admin wallet
        walletRepo.save(wallet);

        /*logging transactions when charging is happened from a user wallet*/
        transactionService.saveTransactionForWithdrawal(wallet, amount);

        /* notification to a user about transaction happened */
        notificationService.sendNotificationToUser(wallet.getUser().getId(),
                amount + " UZS has been charged from your wallet");
    }

    @Transactional
    public Response fillUserWallet(DepositToWallet request) {
        Wallet wallet = walletRepo.findById(request.walletId())
                .orElseThrow(() -> new NoSuchElementException("wallet not found"));
        BigDecimal currentBalance = wallet.getBalance();
        BigDecimal updatedBalance = currentBalance.add(request.amount());
        wallet.setBalance(updatedBalance);
        wallet.setUpdatedAt(LocalDateTime.now());
        walletRepo.save(wallet);

        transactionService.saveTransactionForDepositing(wallet, request.amount());

        /* notification to a user about transaction happened */
        notificationService.sendNotificationToUser(wallet.getUser().getId(),
                request.amount() + " UZS has been credited to your wallet\n " +
                        "your current balance is " + updatedBalance);

        return new Response("updated user wallet", true);
    }

    public Response sendPaymentCheck(UUID userId, MultipartFile file) {
        Users user = usersRepo.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("user not found"));

        try {
            byte[] fileData = file.getBytes();
            PaymentCheck check = new PaymentCheck();
            check.setFilename(file.getOriginalFilename());
            check.setFileType(file.getContentType());
            check.setFile(fileData);
            check.setUploadedBy(user);
            check.setUploadDate(LocalDateTime.now());
            check.setUpdatedDate(LocalDateTime.now());
            paymentCheckRepo.save(check);

            UUID adminId = usersRepo.findByUserRole(Users.UserRole.ROLE_ADMIN).getId();

            /* notification to admin about a file sent by a user */
            notificationService.sendNotificationToUser(adminId,
                    user.getFullName() + " has sent a file " + file.getOriginalFilename() +
                            "\n please check it");

            return new Response("file sent successfully", true);
        } catch (IOException e) {
            return new Response(e.getMessage(), false);
        }
    }
}
