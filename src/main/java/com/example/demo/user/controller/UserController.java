package com.example.demo.user.controller;

import com.example.demo.common.wrapper.ApiResponseWrapper;
import com.example.demo.security.jwt.dto.JwtTokenDto;
import com.example.demo.security.jwt.service.JwtService;
import com.example.demo.user.model.dto.*;
import com.example.demo.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponseWrapper<UserReadDto>> register(@RequestBody @Valid UserCreateDto dto) {
        UserReadDto data = userService.createUser(dto);
        return ResponseEntity.ok(new ApiResponseWrapper<>("User registered successfully", data));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseWrapper<JwtTokenDto>> login(@RequestBody @Valid UserLoginDto dto) {
        JwtTokenDto data = jwtService.generateToken(dto);
        return ResponseEntity.ok(new ApiResponseWrapper<>("Login successful", data));
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponseWrapper<UserReadDto>> updateUser(
            @RequestBody @Valid UserUpdateDto dto,
            Authentication authentication) {

        UserReadDto data = userService.updateUser(dto, authentication);
        return ResponseEntity.ok(new ApiResponseWrapper<>("User updated successfully", data));
    }

    @PostMapping("/update-password")
    public ResponseEntity<ApiResponseWrapper<Void>> changePassword(
            @RequestBody @Valid PasswordUpdateDto dto,
            Authentication authentication) {

        userService.updatePassword(dto, authentication);
        return ResponseEntity.ok(new ApiResponseWrapper<>("Password updated successfully", null));
    }

    @PostMapping("/delete")
    public ResponseEntity<ApiResponseWrapper<Void>> deleteUser(Authentication authentication) {
        userService.deleteUser(authentication);
        return ResponseEntity.ok(new ApiResponseWrapper<>("User deleted successfully", null));
    }

}
