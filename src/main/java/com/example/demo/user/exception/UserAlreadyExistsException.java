package com.example.demo.user.exception;

import com.example.demo.common.exception.CommonException;

public class UserAlreadyExistsException extends CommonException {

    public UserAlreadyExistsException(String fieldName, String fieldValue) {
        super("User with this %s already exists: %s".formatted(fieldName, fieldValue));
    }

}
