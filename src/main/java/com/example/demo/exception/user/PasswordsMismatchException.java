package com.example.demo.exception.user;

import com.example.demo.exception.base.BaseException;

public class PasswordsMismatchException extends BaseException {

    public PasswordsMismatchException() {
        super("Passwords do not match");
    }

}
