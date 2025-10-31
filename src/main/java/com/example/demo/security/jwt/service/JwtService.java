package com.example.demo.security.jwt.service;

import com.example.demo.security.jwt.dto.JwtTokenDto;
import com.example.demo.user.model.dto.UserLoginDto;
import com.example.demo.user.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;
    private final UserService userService;

    private final long expirationIn;

    public JwtService(JwtEncoder jwtEncoder, UserService userService, @Value("${app.security.jwt.expiration}") long expirationIn) {
        this.jwtEncoder = jwtEncoder;
        this.userService = userService;
        this.expirationIn = expirationIn;
    }

    public JwtTokenDto generateToken(UserLoginDto dto) {
        Long userId = userService.authenticateAndGetCurrentUserId(dto);
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("reminder-app")
                .subject(String.valueOf(userId))
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expirationIn))
                .build();
        return new JwtTokenDto(jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue(), expirationIn);
    }

}
