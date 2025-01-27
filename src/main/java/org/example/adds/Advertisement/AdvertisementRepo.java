package org.example.adds.Advertisement;

import org.example.adds.Users.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdvertisementRepo extends JpaRepository<Advertisement, UUID> {
    Optional<Advertisement> findByAdvLink(String advLink);

    List<Advertisement> findByUser(Users users);

}
