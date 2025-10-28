package com.example.demo.reminder.event.dto;

import com.example.demo.reminder.model.entity.Reminder;

public record ReminderEventDto(Reminder reminder, Type type) {

    public enum Type {
        CREATED,
        UPDATED,
        DELETED
    }

}