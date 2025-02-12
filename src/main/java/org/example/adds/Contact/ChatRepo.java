package org.example.adds.Contact;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatRepo extends JpaRepository<Chat, UUID> {

    boolean existsByChatName(String chatName);

    List<Chat> findByChatName(String chatName);

    boolean existsByUserId(UUID userId);

    List<Chat> findByUserId(UUID userId);
}
