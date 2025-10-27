package com.example.demo.user.model.dto;

import com.example.demo.validation.annotation.ValidField;
import jakarta.validation.constraints.Email;

public record UserCreateDto(
        @ValidField(name = "Username", nullable = false, allowSpaces = false, maxSize = 50)
        String username,
        @ValidField(name = "Password", nullable = false, allowSpaces = false, minSize = 8, maxSize = 50)
        String password,
        @ValidField(name = "RepeatPassword", nullable = false, allowSpaces = false, minSize = 8, maxSize = 50)
        String repeatPassword,
        @ValidField(name = "Email", allowSpaces = false, maxSize = 50) @Email
        String email,
        @ValidField(name = "Telegram", allowSpaces = false, maxSize = 50)
        String telegram
        ) {}
