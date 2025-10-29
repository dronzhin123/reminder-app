package com.example.demo.exception.user;

import com.example.demo.exception.base.BaseException;

public class UserNotFoundException extends BaseException {

    public UserNotFoundException(Long userId) {
        super(String.format("User not found with id: %d", userId));
    }

    public UserNotFoundException(String username) {
        super(String.format("User not found with username: %s", username));
    }

}
