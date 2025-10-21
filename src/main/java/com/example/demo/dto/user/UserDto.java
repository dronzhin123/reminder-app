package com.example.demo.dto.user;

import com.example.demo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value @AllArgsConstructor
public class UserDto {
    Long id;
    String username;
    String email;
    User.Role role;
}
