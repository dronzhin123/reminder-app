package com.example.demo.user.model.dto;

import com.example.demo.validation.annotation.ValidField;

public record PasswordUpdateDto(
        @ValidField(name = "Password", nullable = false, allowSpaces = false, minSize = 8, maxSize = 50)
        String password,
        @ValidField(name = "RepeatPassword", nullable = false, allowSpaces = false, minSize = 8, maxSize = 50)
        String repeatPassword) {}
