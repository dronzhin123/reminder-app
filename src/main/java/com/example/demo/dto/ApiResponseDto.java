package com.example.demo.dto;

import lombok.Value;

@Value
public class ApiResponseDto<T> {
    String message;
    T data;
}
