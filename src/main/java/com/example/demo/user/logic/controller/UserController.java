package com.example.demo.user.logic.controller;

import com.example.demo.user.model.dto.ChangePasswordDto;
import com.example.demo.user.model.entity.User;
import com.example.demo.user.logic.service.UserService;
import com.example.demo.wrapper.ApiResponseWrapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponseWrapper<Void>> changePassword(
            @RequestBody @Valid ChangePasswordDto dto,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();
        userService.changePassword(user, dto.password());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponseWrapper<>("Password changed successfully", null));
    }

}
