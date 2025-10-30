package com.example.demo.common.wrapper;

public record ApiResponseWrapper<T>(String message, T data) {}
