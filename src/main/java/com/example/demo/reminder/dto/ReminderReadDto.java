package com.example.demo.reminder.dto;

import java.time.LocalDateTime;

public record ReminderReadDto(Long id, String title, String description, LocalDateTime reminderDateTime) {}
