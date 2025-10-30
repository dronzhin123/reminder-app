package com.example.demo.common.validation.annotation;

import com.example.demo.common.validation.validator.FieldValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FieldValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface Field {

    String message() default "Invalid field value";

    String name() default "Field";

    boolean nullable() default true;

    boolean allowSpaces() default true;

    int minSize() default 0;

    int maxSize() default Integer.MAX_VALUE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}