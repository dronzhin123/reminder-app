package com.example.demo.user.validation.validator;

import com.example.demo.user.validation.annotation.PasswordsMatch;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Method;

public class PasswordsMatchValidator implements ConstraintValidator<PasswordsMatch, Object> {

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        if (obj == null) {
            return true;
        }
        try {
            Method getPassword = obj.getClass().getMethod("password");
            Method getRepeatPassword = obj.getClass().getMethod("repeatPassword");
            String password = (String) getPassword.invoke(obj);
            String repeatPassword = (String) getRepeatPassword.invoke(obj);
            if (password == null || repeatPassword == null) {
                return true;
            }
            boolean matches = password.equals(repeatPassword);
            if (!matches) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Passwords do not match")
                        .addPropertyNode("repeatPassword")
                        .addConstraintViolation();
            }
            return matches;
        } catch (Exception e) {
            return true;
        }
    }

}