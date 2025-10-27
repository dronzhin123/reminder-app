package com.example.demo.user.model.dto;

public record UserReadDto(Long id,
                          String username,
                          String email,
                          String telegram) {}
