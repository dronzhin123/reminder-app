package com.example.demo.exception;

public class PasswordsNotMatchException extends RuntimeException {

    public PasswordsNotMatchException() {
        super("Password and password confirmation not match");
    }

}
