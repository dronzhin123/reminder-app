package com.example.demo.jwt.service;

import com.example.demo.user.model.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;

    public final long expirationIn;

    public JwtService(JwtEncoder jwtEncoder, @Value("${app.security.jwt.expiration}") long expirationIn) {
        this.jwtEncoder = jwtEncoder;
        this.expirationIn = expirationIn;
    }

    public String generateToken(User user) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("reminder-app")
                .subject(user.getUsername())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expirationIn))
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

}
