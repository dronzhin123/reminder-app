package com.example.demo.user.dto;

import com.example.demo.validation.annotation.Field;
import jakarta.validation.constraints.Email;

public record UserCreateDto(
        @Field(name = "Username", nullable = false, allowSpaces = false, maxSize = 50)
        String username,
        @Field(name = "Password", nullable = false, allowSpaces = false, minSize = 8, maxSize = 50)
        String password,
        @Field(name = "Password", nullable = false, allowSpaces = false, minSize = 8, maxSize = 50)
        String confirmPassword,
        @Field(name = "Email", nullable = false, allowSpaces = false, maxSize = 50) @Email
        String email) {}
