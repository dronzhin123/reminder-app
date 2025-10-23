package com.example.demo.handler;

import com.example.demo.exception.EntityAlreadyExistsException;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.exception.PasswordsNotMatchException;
import com.example.demo.wrapper.ApiResponseWrapper;
import com.example.demo.wrapper.ErrorWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponseWrapper<Object>> handleEntityNotFoundException(EntityNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponseWrapper<>(exception.getMessage(), null, null));
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ApiResponseWrapper<Object>> handleEntityAlreadyExistsException(EntityAlreadyExistsException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiResponseWrapper<>(exception.getMessage(), null, null));
    }

    @ExceptionHandler(PasswordsNotMatchException.class)
    public ResponseEntity<ApiResponseWrapper<Object>> handlePasswordsNotMatchException(PasswordsNotMatchException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponseWrapper<>(exception.getMessage(), null, null));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponseWrapper<Object>> handleBadCredentials(BadCredentialsException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponseWrapper<>(exception.getMessage(), null, null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseWrapper<Object>> handleValidationException(MethodArgumentNotValidException exception) {
        Map<String, String> details = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error ->
                details.put(error.getField(), error.getDefaultMessage())
        );
        ErrorWrapper errorWrapper = new ErrorWrapper("Validation failed", details);
        ApiResponseWrapper<Object> responseWrapper = new ApiResponseWrapper<>("Validation failed", null, errorWrapper);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseWrapper);
    }


}
