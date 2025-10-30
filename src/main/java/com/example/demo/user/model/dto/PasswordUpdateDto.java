package com.example.demo.user.model.dto;

import com.example.demo.user.validation.annotation.PasswordsMatch;
import com.example.demo.common.validation.annotation.Field;

@PasswordsMatch
public record PasswordUpdateDto(
        @Field(name = "Password", nullable = false, allowSpaces = false, minSize = 8, maxSize = 50)
        String password,
        @Field(name = "RepeatPassword", nullable = false, allowSpaces = false, minSize = 8, maxSize = 50)
        String repeatPassword) {}
