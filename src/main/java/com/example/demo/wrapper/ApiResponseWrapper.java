package com.example.demo.wrapper;

public record ApiResponseWrapper<T>(String message, T data, ErrorWrapper errors) {
}
