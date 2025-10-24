package com.example.demo.user.logic.service;

import com.example.demo.exception.EntityAlreadyExistsException;
import com.example.demo.user.model.dto.RegisterDto;
import com.example.demo.user.model.dto.UserReadDto;
import com.example.demo.user.model.entity.User;
import com.example.demo.user.model.mapper.UserMapper;
import com.example.demo.user.logic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserReadDto saveUser(RegisterDto dto) {
        if (userRepository.existsByUsername(dto.username())) {
            throw new EntityAlreadyExistsException(User.class, "username", dto.username());
        }
        if (userRepository.existsByEmail(dto.email())) {
            throw new EntityAlreadyExistsException(User.class, "email", dto.email());
        }
        User user = userMapper.toUser(dto);
        user.setPassword(passwordEncoder.encode(dto.password()));
        return userMapper.toDto(userRepository.save(user));
    }

    public void changePassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

}
