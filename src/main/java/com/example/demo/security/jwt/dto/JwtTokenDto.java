package com.example.demo.security.jwt.dto;

public record JwtTokenDto(String token, Long expirationIn) {}
