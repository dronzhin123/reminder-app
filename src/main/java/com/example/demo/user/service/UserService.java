package com.example.demo.user.service;

import com.example.demo.user.exception.UserAlreadyExistsException;
import com.example.demo.user.exception.UserNotFoundException;
import com.example.demo.user.model.dto.*;
import com.example.demo.user.model.entity.User;
import com.example.demo.user.model.mapper.UserMapper;
import com.example.demo.user.repository.UserRepository;
import com.example.demo.user.specification.UserSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public User getUser(UserLoginDto dto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.username(), dto.password()));
        return userRepository.findByUsername(dto.username()).orElseThrow(() -> new UserNotFoundException(dto.username()));
    }

    public User getUser(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Transactional
    public UserReadDto createUser(UserCreateDto dto) {
        validateUserNotExists(dto.username(), dto.email(), dto.telegram(), null);
        User user = userMapper.toUser(dto, passwordEncoder.encode(dto.password()));
        return userMapper.toDto(userRepository.save(user));
    }

    @Transactional
    public UserReadDto updateUser(User user, UserUpdateDto dto) {
        validateUserNotExists(dto.username(), dto.email(), dto.telegram(), user.getId());
        userMapper.update(user, dto);
        return userMapper.toDto(userRepository.save(user));
    }

    @Transactional
    public void updatePassword(User user, PasswordUpdateDto dto) {
        user.setPassword(passwordEncoder.encode(dto.password()));
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    private void validateUserNotExists(String username, String email, String telegram, Long userId) {
        Specification<User> specification = UserSpecification.withUsernameOrEmailOrTelegram(username, email, telegram, userId);
        User user = userRepository.findOne(specification).orElse(null);
        if (user == null) return;
        if (Objects.equals(user.getUsername(), username)) {
            throw new UserAlreadyExistsException("username", username);
        }
        if (Objects.equals(user.getEmail(), email)) {
            throw new UserAlreadyExistsException("email", email);
        }
        if (Objects.equals(user.getTelegram(), telegram)) {
            throw new UserAlreadyExistsException("telegram", telegram);
        }
    }

}