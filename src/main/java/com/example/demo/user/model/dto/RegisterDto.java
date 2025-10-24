package com.example.demo.user.model.dto;

import com.example.demo.validation.annotation.ValidField;
import com.example.demo.validation.annotation.PasswordMatches;
import jakarta.validation.constraints.Email;

@PasswordMatches
public record RegisterDto(
        @ValidField(name = "Username", nullable = false, allowSpaces = false, maxSize = 50)
        String username,
        @ValidField(name = "Password", nullable = false, allowSpaces = false, minSize = 8, maxSize = 50)
        String password,
        @ValidField(name = "ConfirmPassword", nullable = false, allowSpaces = false, minSize = 8, maxSize = 50)
        String confirmPassword,
        @ValidField(name = "Email", nullable = false, allowSpaces = false, maxSize = 50) @Email
        String email) {}
