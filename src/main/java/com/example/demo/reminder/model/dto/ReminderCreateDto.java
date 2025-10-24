package com.example.demo.reminder.model.dto;

import com.example.demo.validation.annotation.ValidField;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ReminderCreateDto(
        @ValidField(name = "Title", nullable = false, maxSize = 255)
        String title,
        @ValidField(name = "Description", maxSize = 4096)
        String description,
        @NotNull(message = "Reminder date/time must not be null")
        @Future(message = "Reminder date/time must be in the future")
        LocalDateTime reminderDateTime) {}
