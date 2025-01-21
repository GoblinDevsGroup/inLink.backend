package org.example.adds.Visitors;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VisitorsRepo extends JpaRepository<Visitors, UUID> {

    boolean existsByIp(String ip);
}
