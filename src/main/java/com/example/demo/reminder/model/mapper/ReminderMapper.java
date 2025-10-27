package com.example.demo.reminder.model.mapper;

import com.example.demo.reminder.model.dto.ReminderCreateDto;
import com.example.demo.reminder.model.dto.ReminderReadDto;
import com.example.demo.reminder.model.dto.ReminderUpdateDto;
import com.example.demo.reminder.model.entity.Reminder;
import com.example.demo.user.model.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ReminderMapper {

    ReminderReadDto toDto(Reminder reminder);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "user", source = "user")
    Reminder toReminder(ReminderCreateDto dto, User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget Reminder reminder, ReminderUpdateDto dto);

}
