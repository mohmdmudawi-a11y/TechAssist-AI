package com.techassist.backend.repository;

import com.techassist.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repository for User entities.
 *
 * By extending JpaRepository, we automatically get:
 *   - save(user)
 *   - findById(id)
 *   - findAll()
 *   - delete(user)
 *   - count()
 *   - and many more...
 *
 * We only need to declare extra query methods we need.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find a user by their email address.
     * Used during login.
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if an email already exists.
     * Used during registration to prevent duplicates.
     */
    boolean existsByEmail(String email);
}