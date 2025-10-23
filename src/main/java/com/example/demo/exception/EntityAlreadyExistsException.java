package com.example.demo.exception;

public class EntityAlreadyExistsException extends RuntimeException {

    public EntityAlreadyExistsException(Class<?> entityClass, String fieldName, Object fieldValue) {
        super(entityClass.getSimpleName() + " with " + fieldName +  " " + fieldValue + " already exists");
    }

}
