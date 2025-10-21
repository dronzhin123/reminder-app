package com.example.demo.dto.token;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value @AllArgsConstructor
public class TokenDto {
    String token;
    String format;
    String expiration;
}
