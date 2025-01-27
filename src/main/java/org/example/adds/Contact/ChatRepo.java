package org.example.adds.Contact;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatRepo extends JpaRepository<Chat, UUID> {

    boolean existsBySenderId(UUID from);

    Chat findBySenderId(UUID from);

    List<Chat> findByChatName(String chatName);
}
