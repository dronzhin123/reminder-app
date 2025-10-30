package com.example.demo.user.model.dto;

import com.example.demo.common.validation.annotation.Field;
import jakarta.validation.constraints.Email;

public record UserUpdateDto(
        @Field(name = "Username", nullable = false, allowSpaces = false, maxSize = 50)
        String username,
        @Field(name = "Email", allowSpaces = false, maxSize = 50) @Email
        String email,
        @Field(name = "Telegram", allowSpaces = false, maxSize = 50)
        String telegram) {}
