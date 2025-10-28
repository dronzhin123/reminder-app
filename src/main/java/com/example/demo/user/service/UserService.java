package com.example.demo.user.service;

import com.example.demo.user.jpa.repository.UserRepository;
import com.example.demo.user.model.dto.*;
import com.example.demo.user.model.entity.User;
import com.example.demo.user.model.mapper.UserMapper;
import com.example.demo.user.valiadtor.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserValidator userValidator;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserReadDto createUser(UserCreateDto dto) {
        userValidator.validatePasswords(dto.password(), dto.repeatPassword());
        userValidator.validateUser(dto.username(), dto.email(), dto.telegram());
        User user = userMapper.toUser(dto, passwordEncoder.encode(dto.password()));
        return userMapper.toDto(userRepository.save(user));
    }

    public User getUser(UserLoginDto dto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.username(), dto.password()));
        return userRepository.findByUsername(dto.username())
                .orElseThrow(() -> new RuntimeException("User not found with username: %s".formatted(dto.username())));
    }

    public User getUser(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: %d".formatted(userId)));
    }

    @Transactional
    public UserReadDto updateUser(User user, UserUpdateDto dto) {
        userValidator.validateUser(user.getId(), dto.username(), dto.email(), dto.telegram());
        userMapper.update(user, dto);
        return userMapper.toDto(userRepository.save(user));
    }

    @Transactional
    public void updatePassword(User user, PasswordUpdateDto dto) {
        userValidator.validatePasswords(dto.password(), dto.repeatPassword());
        user.setPassword(passwordEncoder.encode(dto.password()));
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

}