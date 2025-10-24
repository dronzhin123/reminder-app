package com.example.demo.reminder.model.entity;

import com.example.demo.user.model.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity @Table(name = "reminders")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Reminder {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private LocalDateTime reminderDateTime;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

}
