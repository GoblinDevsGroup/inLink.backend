package org.example.adds.Wallet;

import org.example.adds.Users.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WalletRepo extends JpaRepository<Wallet, UUID> {

    boolean existsByUser(Users user);
}
