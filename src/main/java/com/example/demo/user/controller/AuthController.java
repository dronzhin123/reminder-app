package com.example.demo.user.controller;

import com.example.demo.security.jwt.dto.JwtTokenDto;
import com.example.demo.security.jwt.service.JwtService;
import com.example.demo.user.model.dto.UserCreateDto;
import com.example.demo.user.model.dto.UserLoginDto;
import com.example.demo.user.model.dto.UserReadDto;
import com.example.demo.user.model.entity.User;
import com.example.demo.user.service.UserService;
import com.example.demo.wrapper.ApiResponseWrapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponseWrapper<UserReadDto>> register(@RequestBody @Valid UserCreateDto dto) {
        UserReadDto data = userService.createUser(dto);
        return ResponseEntity.ok(new ApiResponseWrapper<>("User registered successfully", data));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseWrapper<JwtTokenDto>> login(@RequestBody @Valid UserLoginDto dto) {
        User user = userService.getUser(dto);
        JwtTokenDto data = jwtService.generateToken(user);
        return ResponseEntity.ok(new ApiResponseWrapper<>("Login successful", data));
    }

}