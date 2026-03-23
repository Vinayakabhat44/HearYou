package com.mitraai.auth.repository;

import com.mitraai.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameOrMobileNumber(String username, String mobileNumber);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Boolean existsByMobileNumber(String mobileNumber);

    List<User> findByUsernameContainingOrEmailContainingOrMobileNumberContaining(String username, String email,
            String mobileNumber);
}
