package com.example.demo.controller;

import com.example.demo.dto.ApiResponseDto;
import com.example.demo.dto.token.TokenDto;
import com.example.demo.dto.user.UserCreateDto;
import com.example.demo.dto.user.UserDto;
import com.example.demo.dto.user.UserLoginDto;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.JwtService;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.demo.util.TimeFormatter.sToHMS;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDto<UserDto>> register(@RequestBody @Valid UserCreateDto dto) {
        if (userService.existsByUsername(dto.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponseDto<>("Username already exists", null));
        }
        try {
            User user = userService.save(userMapper.toUser(dto));
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponseDto<>("User registered successfully", userMapper.toUserDto(user)));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDto<>("Invalid user data", null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<TokenDto>> login(@RequestBody @Valid UserLoginDto dto) {
        try {
            Authentication authentication = userMapper.toAuthentication(dto);
            User user = (User) authentication.getPrincipal();
            String token = jwtService.generateToken(user);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponseDto<>("Login successful",
                            new TokenDto(token, "Bearer", sToHMS(jwtService.getExpiration()))));
        }
        catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponseDto<>("Invalid username or password", null));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDto<>("Internal server error", null));
        }
    }

}