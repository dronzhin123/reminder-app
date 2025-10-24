package com.example.demo.user.logic.controller;

import com.example.demo.user.model.dto.RegisterDto;
import com.example.demo.wrapper.ApiResponseWrapper;
import com.example.demo.jwt.dto.JwtTokenDto;
import com.example.demo.user.model.dto.UserReadDto;
import com.example.demo.user.model.dto.LoginDto;
import com.example.demo.user.model.entity.User;
import com.example.demo.jwt.service.JwtService;
import com.example.demo.user.logic.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<ApiResponseWrapper<UserReadDto>> register(@RequestBody @Valid RegisterDto dto) {
        UserReadDto data = userService.saveUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseWrapper<>("User registered successfully", data));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseWrapper<JwtTokenDto>> login(@RequestBody @Valid LoginDto dto) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(dto.username(), dto.password()));
        User user = (User) authentication.getPrincipal();
        JwtTokenDto data = new JwtTokenDto(jwtService.generateToken(user), jwtService.expirationIn);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseWrapper<>("Login successful", data));
    }

}