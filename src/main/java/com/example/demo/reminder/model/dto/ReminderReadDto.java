package com.example.demo.reminder.model.dto;

import com.example.demo.reminder.model.entity.Reminder;

import java.time.LocalDateTime;

public record ReminderReadDto(Long id,
                              String title,
                              String description,
                              LocalDateTime remindAt,
                              LocalDateTime createdAt,
                              Reminder.Sender sender,
                              Reminder.Status status) {}
