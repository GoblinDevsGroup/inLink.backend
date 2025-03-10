package org.example.adds.Files;

import org.example.adds.Chat.Files;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepo extends JpaRepository<Files, Long> {
}
