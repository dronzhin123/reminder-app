package com.example.demo.reminder.jpa.repository;

import com.example.demo.reminder.model.entity.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ReminderRepository extends JpaRepository<Reminder, Long>, JpaSpecificationExecutor<Reminder> {

    boolean existsByTitleAndUserId(String title, Long userId);

    boolean existsByTitleAndUserIdAndIdNot(String title, Long userId, Long reminderId);

    Optional<Reminder> findByIdAndUserId(Long id, Long userId);

}
