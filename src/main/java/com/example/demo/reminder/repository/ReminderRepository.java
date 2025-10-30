package com.example.demo.reminder.repository;

import com.example.demo.reminder.model.entity.Reminder;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ReminderRepository extends JpaRepository<Reminder, Long>, JpaSpecificationExecutor<Reminder> {

    Optional<Reminder> findByIdAndUserId(Long id, Long userId);

    @EntityGraph(attributePaths = "user")
    Optional<Reminder> findWithUserById(Long id);

}
