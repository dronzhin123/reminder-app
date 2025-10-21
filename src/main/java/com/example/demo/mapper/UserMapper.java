package com.example.demo.mapper;

import com.example.demo.dto.user.UserCreateDto;
import com.example.demo.dto.user.UserDto;
import com.example.demo.dto.user.UserLoginDto;
import com.example.demo.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public User toUser(UserCreateDto dto) {
        return User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .email(dto.getEmail())
                .build();
    }

    public UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
    }

    public Authentication toAuthentication(UserLoginDto dto) {
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
    }

}

