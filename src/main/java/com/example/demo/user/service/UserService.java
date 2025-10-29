package com.example.demo.user.service;

import com.example.demo.exception.user.PasswordsMismatchException;
import com.example.demo.exception.user.UserAlreadyExistsException;
import com.example.demo.exception.user.UserNotFoundException;
import com.example.demo.user.jpa.repository.UserRepository;
import com.example.demo.user.model.dto.*;
import com.example.demo.user.model.entity.User;
import com.example.demo.user.model.mapper.UserMapper;
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
        userValidator.validateUserNotExists(dto.username(), dto.email(), dto.telegram());
        User user = userMapper.toUser(dto, passwordEncoder.encode(dto.password()));
        return userMapper.toDto(userRepository.save(user));
    }

    public User getUser(UserLoginDto dto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.username(), dto.password()));
        return userRepository.findByUsername(dto.username()).orElseThrow(() -> new UserNotFoundException(dto.username()));
    }

    public User getUser(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Transactional
    public UserReadDto updateUser(User user, UserUpdateDto dto) {
        userValidator.validateUserNotExists(user.getId(), dto.username(), dto.email(), dto.telegram());
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

    private class UserValidator {

        public void validatePasswords(String password, String repeatPassword) {
            if (!password.equals(repeatPassword)) {
                throw new PasswordsMismatchException();
            }
        }

        public void validateUserNotExists(String username, String email, String telegram) {
            if (username != null && userRepository.existsByUsername(username)) {
                throw new UserAlreadyExistsException("username", username);
            }
            if (email != null && userRepository.existsByEmail(email)) {
                throw new UserAlreadyExistsException("email", email);
            }
            if (telegram != null && userRepository.existsByTelegram(telegram)) {
                throw new UserAlreadyExistsException("telegram", telegram);
            }
        }

        public void validateUserNotExists(Long userId, String username, String email, String telegram) {
            if (username != null && userRepository.existsByUsernameAndIdNot(username, userId)) {
                throw new UserAlreadyExistsException("username", username);
            }
            if (email != null && userRepository.existsByEmailAndIdNot(email, userId)) {
                throw new UserAlreadyExistsException("email", email);
            }
            if (telegram != null && userRepository.existsByTelegramAndIdNot(telegram, userId)) {
                throw new UserAlreadyExistsException("telegram", telegram);
            }
        }

    }


}