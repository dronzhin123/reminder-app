package com.example.demo.user.model.dto;

import com.example.demo.common.validation.annotation.Field;

public record PasswordUpdateDto(
        @Field(name = "Password", nullable = false, allowSpaces = false, minSize = 8, maxSize = 50)
        String password) {}
