package com.example.demo.reminder.model.dto;

import com.example.demo.reminder.model.entity.Reminder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ReminderFilterDto {

    @Builder.Default
    private int page = 0;

    @Builder.Default
    private int size = 10;

    @Builder.Default
    private String sortField = "createdAt";

    @Builder.Default
    private String direction = "asc";

    private String keyword;

    private LocalDateTime createdAtStart;

    private LocalDateTime createdAtEnd;

    private LocalDateTime remindAtStart;

    private LocalDateTime remindAtEnd;

    private Reminder.Sender sender;

    private Reminder.Status status;

}
