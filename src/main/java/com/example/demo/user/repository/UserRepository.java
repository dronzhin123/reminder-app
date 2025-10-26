package com.example.demo.user.repository;

import com.example.demo.user.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByTelegram(String telegram);

    boolean existsByUsernameAndIdNot(String username, Long userId);

    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByTelegramAndIdNot(String telegram, Long id);

}
