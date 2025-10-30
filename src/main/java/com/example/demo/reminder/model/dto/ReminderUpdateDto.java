package com.example.demo.reminder.model.dto;

import com.example.demo.common.validation.annotation.Field;
import com.example.demo.reminder.model.entity.Reminder;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ReminderUpdateDto(
        @Field(name = "Title", maxSize = 255)
        String title,
        @Field(name = "Description", maxSize = 4096)
        String description,
        @NotNull(message = "Reminder date/time must not be null")
        @Future(message = "Reminder date/time must be in the future")
        LocalDateTime remindAt,
        @NotNull(message = "Sender message type must not be null")
        Reminder.Sender sender) {}
