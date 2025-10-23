package com.example.demo.reminder.dto;

import com.example.demo.validation.annotation.Field;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ReminderCreateDto(
        @Field(name = "Title", nullable = false, maxSize = 255)
        String title,
        @Field(name = "Description", maxSize = 4096)
        String description,
        @NotNull(message = "Reminder date/time must not be null")
        @Future(message = "Reminder date/time must be in the future")
        LocalDateTime reminderDateTime) {}
