package com.example.demo.validation.validator;

import com.example.demo.validation.annotation.Field;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FieldValidator implements ConstraintValidator<Field, String> {

    private String fieldName;
    private boolean nullable;
    private boolean allowSpaces;
    private int minSize;
    private int maxSize;

    @Override
    public void initialize(Field constraintAnnotation) {
        this.fieldName = constraintAnnotation.name();
        this.nullable = constraintAnnotation.nullable();
        this.allowSpaces = constraintAnnotation.allowSpaces();
        this.minSize = constraintAnnotation.minSize();
        this.maxSize = constraintAnnotation.maxSize();
    }

    @Override
    public boolean isValid(String field, ConstraintValidatorContext context) {
        if (field == null) {
            if (nullable) return true;
            buildViolation(context, fieldName + " cannot be null");
            return false;
        }

        if (field.trim().isEmpty()) {
            if (nullable) return true;
            buildViolation(context, fieldName + " cannot be blank or only spaces");
            return false;
        }

        int size = field.length();
        if (size < minSize || size > maxSize) {
            buildViolation(context, fieldName + " must be between " + minSize + " and " + maxSize + " characters");
            return false;
        }

        if (!allowSpaces && field.contains(" ")) {
            buildViolation(context, fieldName + " cannot contain spaces");
            return false;
        }

        return true;
    }

    private void buildViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }
}
