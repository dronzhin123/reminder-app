package com.example.demo.user.model.dto;

import com.example.demo.validation.annotation.ValidField;

public record UserLoginDto(
        @ValidField(name = "Username", nullable = false, allowSpaces = false, maxSize = 50)
        String username,
        @ValidField(name = "Password", nullable = false, allowSpaces = false, minSize = 8, maxSize = 50)
        String password) {}

