package org.example.adds.DraftUser;

import org.example.adds.DraftUser.DraftUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DraftUserRepo extends JpaRepository<DraftUsers, Long> {
    Optional<DraftUsers> findByPhone(String phone);
}
