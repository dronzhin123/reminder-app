package com.example.demo.reminder.model.dto;

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
    private String sortBy = "createdAt";

    @Builder.Default
    private String direction = "desc";

    private String keyword;

    private LocalDateTime createdAtStart;

    private LocalDateTime createdAtEnd;

    private LocalDateTime remindAtStart;

    private LocalDateTime remindAtEnd;

}
