package org.example.demo3.domain.user.repository;

import org.example.demo3.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);

    Optional<User> findByProviderAndProviderId(String provider, String providerId);

    Optional<User> findByUsername(String username);

}
