package com.example.demo.reminder.repository;

import com.example.demo.reminder.entity.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    boolean existsByTitleAndUserId(String title, Long userId);
}
