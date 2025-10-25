package com.example.demo.reminder.repository;

import com.example.demo.reminder.model.entity.Reminder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    boolean existsByTitleAndUserId(String title, Long userId);
    boolean existsByTitleAndUserIdAndIdNot(String title, Long userId, Long reminderId);
    Optional<Reminder> findByIdAndUserId(Long id, Long userId);
    Page<Reminder> findByUserId(Long userId, Pageable pageable);
}
