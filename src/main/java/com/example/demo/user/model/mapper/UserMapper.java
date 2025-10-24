package com.example.demo.user.model.mapper;

import com.example.demo.user.model.dto.RegisterDto;
import com.example.demo.user.model.dto.UserReadDto;
import com.example.demo.user.model.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true) @Mapping(target = "role", ignore = true) @Mapping(target = "password", ignore = true)
    User toUser(RegisterDto dto);

    UserReadDto toDto(User user);

}


