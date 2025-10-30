package com.example.demo.user.exception;

import com.example.demo.common.exception.CommonException;

public class UserNotFoundException extends CommonException {

    public UserNotFoundException(Long userId) {
        super(String.format("User not found with id: %d", userId));
    }

    public UserNotFoundException(String username) {
        super(String.format("User not found with username: %s", username));
    }

}
