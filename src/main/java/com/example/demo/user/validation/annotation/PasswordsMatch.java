package com.example.demo.user.validation.annotation;

import jakarta.validation.Constraint;
import org.gradle.internal.impldep.com.google.api.client.auth.openidconnect.IdToken;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordsMatchValidator.class)
@Target(ElementType.TYPE) // применяется к классу (record)
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordsMatch {
    String message() default "Passwords do not match";
    Class<?>[] groups() default {};
    Class<? extends IdToken.Payload>[] payload() default {};
}
