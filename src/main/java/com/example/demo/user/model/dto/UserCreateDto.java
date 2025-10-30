package com.example.demo.user.model.dto;

import com.example.demo.user.validation.annotation.PasswordsMatch;
import com.example.demo.common.validation.annotation.Field;
import jakarta.validation.constraints.Email;

@PasswordsMatch
public record UserCreateDto(
        @Field(name = "Username", nullable = false, allowSpaces = false, maxSize = 50)
        String username,
        @Field(name = "Password", nullable = false, allowSpaces = false, minSize = 8, maxSize = 50)
        String password,
        @Field(name = "RepeatPassword", nullable = false, allowSpaces = false, minSize = 8, maxSize = 50)
        String repeatPassword,
        @Field(name = "Email", allowSpaces = false, maxSize = 50) @Email
        String email,
        @Field(name = "Telegram", allowSpaces = false, maxSize = 50)
        String telegram) {}
