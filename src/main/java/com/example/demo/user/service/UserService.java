package com.example.demo.user.service;

import com.example.demo.exception.EntityAlreadyExistsException;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.exception.PasswordsNotMatchException;
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

    public Authentication getAuthentication(UserLoginDto dto) {
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.username(), dto.password()));
    }

    public User getUser(UserLoginDto dto) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(dto.username(), dto.password()));
        return getUser(authentication);
    }

    public User getUser(Authentication authentication) {
        return findByUsername(authentication.getName());
    }

    public UserReadDto saveUser(UserCreateDto dto) {
        validatePasswordsMatch(dto.password(), dto.repeatPassword());
        validateUsernameUnique(dto.username());
        validateEmailUnique(dto.email());
        validateTelegramUnique(dto.telegram());
        User user = userMapper.toUser(dto);
        user.setPassword(passwordEncoder.encode(dto.password()));
        return userMapper.toDto(userRepository.save(user));
    }

    public UserReadDto updateUser(User user, UserUpdateDto dto) {
        validateEmailUnique(dto.email(), user.getId());
        validateTelegramUnique(dto.telegram(), user.getId());
        userMapper.update(user, dto);
        return userMapper.toDto(userRepository.save(user));
    }

    public void updatePassword(User user, PasswordUpdateDto dto) {
        validatePasswordsMatch(dto.password(), dto.repeatPassword());
        user.setPassword(passwordEncoder.encode(dto.password()));
        userRepository.save(user);
    }

    private User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(User.class, "username", username));
    }

    private void validatePasswordsMatch(String password, String repeatPassword) {
        if (!password.equals(repeatPassword)) {
            throw new PasswordsNotMatchException();
        }
    }

    private void validateUsernameUnique(String username) {
        if (username != null && userRepository.existsByUsername(username)) {
            throw new EntityAlreadyExistsException(User.class, "username", username);
        }
    }

    private void validateEmailUnique(String email) {
        if (email != null && userRepository.existsByEmail(email)) {
            throw new EntityAlreadyExistsException(User.class, "email", email);
        }
    }

    private void validateTelegramUnique(String telegram) {
        if (telegram != null && userRepository.existsByTelegram(telegram)) {
            throw new EntityAlreadyExistsException(User.class, "telegram", telegram);
        }
    }

    private void validateEmailUnique(String email, Long userId) {
        if (email != null && userRepository.existsByEmailAndIdNot(email, userId)) {
            throw new EntityAlreadyExistsException(User.class, "email", email);
        }
    }

    private void validateTelegramUnique(String telegram, Long userId) {
        if (telegram != null && userRepository.existsByTelegramAndIdNot(telegram, userId)) {
            throw new EntityAlreadyExistsException(User.class, "telegram", telegram);
        }
    }

}
