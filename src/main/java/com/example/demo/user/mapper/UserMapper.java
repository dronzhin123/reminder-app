package com.example.demo.user.mapper;

import com.example.demo.user.dto.UserCreateDto;
import com.example.demo.user.dto.UserReadDto;
import com.example.demo.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true) @Mapping(target = "role", ignore = true)
    User toUser(UserCreateDto dto);

    UserReadDto toDto(User user);
}


