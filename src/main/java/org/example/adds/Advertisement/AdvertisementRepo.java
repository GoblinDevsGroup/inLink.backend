package org.example.adds.Advertisement;

import jakarta.annotation.Nullable;
import org.example.adds.Users.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdvertisementRepo extends JpaRepository<Advertisement, UUID>, JpaSpecificationExecutor<Advertisement> {
    Optional<Advertisement> findByAdvLink(String advLink);

    List<Advertisement> findByUser(Users users);


    static Specification<Advertisement> searchSpecification(@Nullable String searchText){
        return (root, query, builder) -> {
            if (searchText != null && !searchText.trim().isEmpty()) {
                String likePattern = "%" + searchText.toLowerCase() + "%";
                return builder.or(
                        builder.like(builder.lower(root.get("title")), likePattern),
                        builder.like(builder.lower(root.get("status")), likePattern)
                );
            }
            return builder.conjunction();
        };
    }

    static Specification<Advertisement> hasUserId(UUID userId) {
        return (root, query, builder) -> builder.equal(root.get("user").get("id"), userId);
    }

}
