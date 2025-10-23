package com.example.demo.jwt.dto;

public record JwtTokenDto(String token, Long expirationIn) {}
