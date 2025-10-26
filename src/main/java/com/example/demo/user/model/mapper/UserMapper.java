package com.example.demo.user.model.mapper;

import com.example.demo.user.model.dto.UserCreateDto;
import com.example.demo.user.model.dto.UserReadDto;
import com.example.demo.user.model.dto.UserUpdateDto;
import com.example.demo.user.model.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "password", source = "password")
    User toUser(UserCreateDto dto, String password);

    UserReadDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget User user, UserUpdateDto dto);

}


