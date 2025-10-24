package com.example.demo.validation.validator;

import com.example.demo.user.model.dto.RegisterDto;
import com.example.demo.validation.annotation.PasswordMatches;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        if (!(obj instanceof RegisterDto dto)) {
            return true;
        }
        if (dto.password().equals(dto.confirmPassword())) {
            return true;
        }
        buildViolation(context);
        return false;
    }

    private void buildViolation(ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("Passwords not match")
                .addConstraintViolation();
    }

}
