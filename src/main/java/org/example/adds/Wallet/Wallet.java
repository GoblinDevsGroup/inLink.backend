package org.example.adds.Wallet;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.adds.Users.Users;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table
@Data
@NoArgsConstructor
public class Wallet implements Serializable {
    @Id
    @GeneratedValue
    private UUID id;

    @JoinColumn(nullable = false)
    private String balanceUuId;

    @OneToOne
    @JoinColumn(nullable = false)
    private Users user;

    @Column(nullable = false)
    private BigDecimal balance;

    @JoinColumn(nullable = false)
    private String daysLeft;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public Wallet(String balanceUuId,
                  Users user,
                  BigDecimal balance) {
        this.balanceUuId = balanceUuId;
        this.user = user;
        this.balance = balance;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
