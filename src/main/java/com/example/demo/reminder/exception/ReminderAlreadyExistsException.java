package com.example.demo.reminder.exception;

import com.example.demo.common.exception.CommonException;

public class ReminderAlreadyExistsException extends CommonException {

    public ReminderAlreadyExistsException(String fieldName, String value) {
        super("A reminder with this %s already exists: %s".formatted(fieldName, value));
    }

}