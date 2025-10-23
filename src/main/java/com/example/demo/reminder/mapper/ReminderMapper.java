package com.example.demo.reminder.mapper;

import com.example.demo.reminder.dto.ReminderCreateDto;
import com.example.demo.reminder.dto.ReminderReadDto;
import com.example.demo.reminder.dto.ReminderUpdateDto;
import com.example.demo.reminder.entity.Reminder;
import com.example.demo.user.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ReminderMapper {

    ReminderReadDto toDto(Reminder reminder);

    default Reminder toReminder(ReminderCreateDto dto, User user) {
        return Reminder.builder()
                .title(dto.title())
                .description(dto.description())
                .reminderDateTime(dto.reminderDateTime())
                .user(user)
                .build();
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true) @Mapping(target = "user", ignore = true)
    void update(@MappingTarget Reminder reminder, ReminderUpdateDto dto);
}
