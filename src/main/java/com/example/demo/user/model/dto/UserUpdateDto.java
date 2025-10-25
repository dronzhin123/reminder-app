package com.example.demo.user.model.dto;

import com.example.demo.validation.annotation.ValidField;
import jakarta.validation.constraints.Email;

public record UserUpdateDto(
        @ValidField(name = "Email", allowSpaces = false, maxSize = 50) @Email
        String email,
        @ValidField(name = "Telegram", allowSpaces = false, maxSize = 50)
        String telegram) {}
