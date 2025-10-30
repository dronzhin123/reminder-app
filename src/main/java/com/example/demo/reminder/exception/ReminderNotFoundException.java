package com.example.demo.reminder.exception;

import com.example.demo.common.exception.CommonException;

public class ReminderNotFoundException extends CommonException {

    public ReminderNotFoundException(Long reminderId) {
        super("Reminder not found with id: %d".formatted(reminderId));
    }

}
