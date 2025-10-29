package com.example.demo.exception.user;

import com.example.demo.exception.base.BaseException;

public class UserAlreadyExistsException extends BaseException {

    public UserAlreadyExistsException(String fieldName, String fieldValue) {
        super("User with this %s already exists: %s".formatted(fieldName, fieldValue));
    }

}
