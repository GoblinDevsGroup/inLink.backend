package org.example.adds.Wallet;

import io.jsonwebtoken.lang.Assert;
import org.example.adds.Admin.Dtos.WalletResponse;
import org.example.adds.Users.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @Mock
    private WalletRepo walletRepo;

    @InjectMocks
    private WalletService walletService;

    private WalletResponse walletResponse;
    private UUID userId;
    private UUID balanceUuId;

    @BeforeEach
    void setUp(){
        userId = UUID.randomUUID();
        balanceUuId = UUID.randomUUID();
        walletResponse = new WalletResponse(
                balanceUuId.toString(),
                userId,
                BigDecimal.valueOf(1000),
                "2",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    void getAllWallets() {
        Wallet wallet = new Wallet();
        wallet.setId(UUID.randomUUID());
        wallet.setBalance(BigDecimal.valueOf(1000));
        wallet.setDaysLeft("2");
        wallet.setUser(new Users());
        wallet.setBalanceUuId(UUID.randomUUID().toString());
        wallet.setCreatedAt(LocalDateTime.now());
        wallet.setUpdatedAt(LocalDateTime.now());

        List<Wallet> walletList = List.of(wallet);
        when(walletRepo.findAll()).thenReturn(walletList);

        List<WalletResponse> response = walletService.getAllWallets();

        verify(walletRepo, times(1)).findAll();

        assertNotNull(response);
        assertEquals(walletList.size(), response.size());
        assertEquals(wallet.getBalanceUuId(), response.get(0).getBalanceUuId());
        assertEquals(wallet.getBalance(), response.get(0).getBalance());
    }

}