package com.example.demo.reminder.model.enums;

public enum ReminderSortField {

    CREATED_AT("createdAt"),
    REMIND_AT("remindAt"),
    TITLE("title");

    public final String value;

    ReminderSortField(String name) {
        this.value = name;
    }

}
