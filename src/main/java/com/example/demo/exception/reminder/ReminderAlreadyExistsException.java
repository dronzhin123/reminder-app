package com.example.demo.exception.reminder;

import com.example.demo.exception.base.BaseException;

public class ReminderAlreadyExistsException extends BaseException {

    public ReminderAlreadyExistsException(String fieldName, String value) {
        super("A reminder with this %s already exists: %s".formatted(fieldName, value));
    }

}