package com.example.demo.user.valiadtor;

import com.example.demo.user.jpa.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;

    public void validatePasswords(String password, String repeatPassword) {
        if (!password.equals(repeatPassword)) {
            throw new RuntimeException("Passwords do not match");
        }
    }

    public void validateUser(String username, String email, String telegram) {
        if (username != null && userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists: %s".formatted(username));
        }
        if (email != null && userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists: %s".formatted(email));
        }
        if (telegram != null && userRepository.existsByTelegram(telegram)) {
            throw new RuntimeException("Telegram already exists: %s".formatted(telegram));
        }
    }

    public void validateUser(Long userId, String username, String email, String telegram) {
        if (username != null && userRepository.existsByUsernameAndIdNot(username, userId)) {
            throw new RuntimeException("Username already exists: %s".formatted(username));
        }
        if (email != null && userRepository.existsByEmailAndIdNot(email, userId)) {
            throw new RuntimeException("Email already exists: %s".formatted(email));
        }
        if (telegram != null && userRepository.existsByTelegramAndIdNot(telegram, userId)) {
            throw new RuntimeException("Telegram already exists: %s".formatted(telegram));
        }
    }
}