package org.example.adds.Chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatRepo extends JpaRepository<Chat, UUID> {
    @Query("select c from Chat c where c.sender.id=:userId or c.receiver.id=:userId order by c.time asc")
    List<Chat> findChatsByUserId(@Param("userId") UUID userId);
}
