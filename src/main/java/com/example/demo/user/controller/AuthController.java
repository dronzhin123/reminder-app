package com.example.demo.user.controller;

import com.example.demo.wrapper.ApiResponseWrapper;
import com.example.demo.jwt.dto.JwtTokenDto;
import com.example.demo.user.dto.UserCreateDto;
import com.example.demo.user.dto.UserReadDto;
import com.example.demo.user.dto.UserLoginDto;
import com.example.demo.user.entity.User;
import com.example.demo.user.mapper.UserMapper;
import com.example.demo.jwt.service.JwtService;
import com.example.demo.user.service.UserService;
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

@RestController @RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<ApiResponseWrapper<UserReadDto>> register(@RequestBody @Valid UserCreateDto dto) {
        User user = userService.save(userMapper.toUser(dto), dto.confirmPassword());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseWrapper<>("User registered successfully", userMapper.toDto(user), null));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseWrapper<JwtTokenDto>> login(@RequestBody @Valid UserLoginDto dto) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(dto.username(), dto.password()));
        User user = (User) authentication.getPrincipal();
        String token = jwtService.generateToken(user);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponseWrapper<>("Login successful",
                        new JwtTokenDto("Bearer " + token, jwtService.expirationIn), null));
    }

}