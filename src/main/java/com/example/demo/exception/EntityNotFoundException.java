package com.example.demo.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Class<?> entityClass, String fieldName, Object fieldValue) {
        super(entityClass.getSimpleName() + " with " + fieldName +  " " + fieldValue + " not found");
    }

}
