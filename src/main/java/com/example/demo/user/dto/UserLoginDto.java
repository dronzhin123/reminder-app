package com.example.demo.user.dto;

import com.example.demo.validation.annotation.Field;

public record UserLoginDto(
        @Field(name = "Username", nullable = false, allowSpaces = false, maxSize = 50)
        String username,
        @Field(name = "Password", nullable = false, allowSpaces = false, minSize = 8, maxSize = 50)
        String password) {}

