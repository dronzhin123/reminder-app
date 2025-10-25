package com.example.demo.user.controller;

import com.example.demo.user.model.dto.PasswordUpdateDto;
import com.example.demo.user.model.dto.UserReadDto;
import com.example.demo.user.model.dto.UserUpdateDto;
import com.example.demo.user.model.entity.User;
import com.example.demo.user.service.UserService;
import com.example.demo.wrapper.ApiResponseWrapper;
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

    @PostMapping("/update-password")
    public ResponseEntity<ApiResponseWrapper<Void>> changePassword(
            @RequestBody @Valid PasswordUpdateDto dto,
            Authentication authentication) {

        User user = userService.getUser(authentication);
        userService.updatePassword(user, dto);
        return ResponseEntity.ok(new ApiResponseWrapper<>("Password updated successfully", null));
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponseWrapper<UserReadDto>> updateUser(
            @RequestBody @Valid UserUpdateDto dto,
            Authentication authentication) {

        User user = userService.getUser(authentication);
        UserReadDto data = userService.updateUser(user, dto);
        return ResponseEntity.ok(new ApiResponseWrapper<>("User updated successfully", data));
    }

}
