package com.example.demo.reminder.model.dto;

import com.example.demo.reminder.model.entity.Reminder;
import com.example.demo.reminder.model.enums.ReminderSortField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ReminderFilterDto {

    @Builder.Default
    private int page = 0;

    @Builder.Default
    private int size = 10;

    @Builder.Default
    private ReminderSortField sortField = ReminderSortField.CREATED_AT;

    @Builder.Default
    private Sort.Direction direction = Sort.Direction.DESC;

    private String keyword;

    private LocalDateTime createdAtStart;

    private LocalDateTime createdAtEnd;

    private LocalDateTime remindAtStart;

    private LocalDateTime remindAtEnd;

    private Reminder.Sender sender;

    private Reminder.Status status;

}
