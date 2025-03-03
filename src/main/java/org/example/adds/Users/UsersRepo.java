package org.example.adds.Users;

import jakarta.annotation.Nullable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsersRepo extends JpaRepository<Users, UUID>, JpaSpecificationExecutor<Users> {
    Optional<Users> findByPhone(String phone);

    Users findByUserRole(Users.UserRole role);

    static Specification<Users> searchSpecification(@Nullable String searchText) {
        return (root, query, builder) -> {
            if (searchText != null && !searchText.trim().isEmpty()) {
                String likePattern = "%" + searchText.toLowerCase() + "%";
                return builder.or(
                        builder.like(builder.lower(root.get("company_name")), likePattern),
                        builder.like(builder.lower(root.get("full_name")), likePattern),
                        builder.like(builder.lower(root.get("phone")), likePattern)
                );
            }
            return builder.conjunction();
        };
    }
}
