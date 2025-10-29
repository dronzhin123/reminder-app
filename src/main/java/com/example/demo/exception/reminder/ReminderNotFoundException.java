package com.example.demo.exception.reminder;

import com.example.demo.exception.base.BaseException;

public class ReminderNotFoundException extends BaseException {

    public ReminderNotFoundException(Long reminderId) {
        super("Reminder not found with id: %d".formatted(reminderId));
    }

}
