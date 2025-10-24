package com.example.demo.user.model.dto;

import com.example.demo.validation.annotation.PasswordMatches;
import com.example.demo.validation.annotation.ValidField;

@PasswordMatches
public record ChangePasswordDto(
        @ValidField(name = "Password", nullable = false, allowSpaces = false, minSize = 8, maxSize = 50)
        String password,
        @ValidField(name = "ConfirmPassword", nullable = false, allowSpaces = false, minSize = 8, maxSize = 50)
        String confirmPassword) {}
