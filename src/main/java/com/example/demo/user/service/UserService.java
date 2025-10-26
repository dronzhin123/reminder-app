package com.example.demo.user.service;

import com.example.demo.user.model.dto.*;
import com.example.demo.user.model.entity.User;
import com.example.demo.user.model.mapper.UserMapper;
import com.example.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public User getUser(UserLoginDto dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.username(), dto.password())
        );
        return userRepository.findByUsername(dto.username())
                .orElseThrow(() -> new RuntimeException("User not found with username: " + dto.username()));
    }

    public User getUser(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }

    public UserReadDto saveUser(UserCreateDto dto) {
        validatePasswordsMatch(dto.password(), dto.repeatPassword());
        validateUserNotExists(dto.username(), dto.email(), dto.telegram());
        User user = userMapper.toUser(dto, passwordEncoder.encode(dto.password()));
        return userMapper.toDto(userRepository.save(user));
    }

    public UserReadDto updateUser(User user, UserUpdateDto dto) {
        validateUserNotExists(user.getId(), dto.username(), dto.email(), dto.telegram());
        userMapper.update(user, dto);
        return userMapper.toDto(userRepository.save(user));
    }

    public void updatePassword(User user, PasswordUpdateDto dto) {
        validatePasswordsMatch(dto.password(), dto.repeatPassword());
        user.setPassword(passwordEncoder.encode(dto.password()));
        userRepository.save(user);
    }

    private void validatePasswordsMatch(String password, String repeatPassword) {
        if (!password.equals(repeatPassword)) {
            throw new RuntimeException("Passwords do not match");
        }
    }

    private void validateUserNotExists(String username, String email, String telegram) {
        if (username != null && userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists: " + username);
        }
        if (email != null && userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists: " + email);
        }
        if (telegram != null && userRepository.existsByTelegram(telegram)) {
            throw new RuntimeException("Telegram already exists: " + telegram);
        }
    }

    private void validateUserNotExists(Long userId, String username, String email, String telegram) {
        if (username != null && userRepository.existsByUsernameAndIdNot(username, userId)) {
            throw new RuntimeException("Username already exists: " + username);
        }
        if (email != null && userRepository.existsByEmailAndIdNot(email, userId)) {
            throw new RuntimeException("Email already exists: " + email);
        }
        if (telegram != null && userRepository.existsByTelegramAndIdNot(telegram, userId)) {
            throw new RuntimeException("Telegram already exists: " + telegram);
        }
    }

}