package com.example.demo.user.service;

import com.example.demo.exception.EntityAlreadyExistsException;
import com.example.demo.exception.EntityNotFoundException;
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
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(dto.username(), dto.password()));
        return userRepository.findByUsername(dto.username())
                .orElseThrow(() -> new EntityNotFoundException(User.class, "username", dto.username()));
    }

    public User getUser(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(User.class, "id", userId));
    }

    public UserReadDto saveUser(UserCreateDto dto) {
        validatePasswordsMatch(dto.password(), dto.repeatPassword());
        validateUserNotExists(dto.username(), dto.email(), dto.telegram());
        User user = userMapper.toUser(dto);
        user.setPassword(passwordEncoder.encode(dto.password()));
        return userMapper.toDto(userRepository.save(user));
    }

    public UserReadDto updateUser(User user, UserUpdateDto dto) {
        validateUserNotExists(user.getId(), dto.email(), dto.telegram());
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
            throw new IllegalArgumentException("Password and password confirmation must match");
        }
    }

    private void validateUserNotExists(String username, String email, String telegram) {
        if (username != null && userRepository.existsByUsername(username)) {
            throw new EntityAlreadyExistsException(User.class, "username", username);
        }
        if (email != null && userRepository.existsByEmail(email)) {
            throw new EntityAlreadyExistsException(User.class, "email", email);
        }
        if (telegram != null && userRepository.existsByTelegram(telegram)) {
            throw new EntityAlreadyExistsException(User.class, "telegram", telegram);
        }
    }

    private void validateUserNotExists(Long userId, String email, String telegram) {
        if (email != null && userRepository.existsByEmailAndIdNot(email, userId)) {
            throw new EntityAlreadyExistsException(User.class, "email", email);
        }
        if (telegram != null && userRepository.existsByTelegramAndIdNot(telegram, userId)) {
            throw new EntityAlreadyExistsException(User.class, "telegram", telegram);
        }
    }

}
