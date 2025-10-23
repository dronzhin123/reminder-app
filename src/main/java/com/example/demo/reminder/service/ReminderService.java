package com.example.demo.reminder.service;

import com.example.demo.exception.EntityAlreadyExistsException;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.reminder.entity.Reminder;
import com.example.demo.reminder.repository.ReminderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class ReminderService {

    private final ReminderRepository reminderRepository;

    public Reminder save(Reminder reminder) {
        String title = reminder.getTitle();
        if (reminderRepository.existsByTitleAndUserId(title, reminder.getUser().getId())) {
            throw new EntityAlreadyExistsException(Reminder.class, "title", title);
        }
        return reminderRepository.save(reminder);
    }

    public Reminder findById(Long reminderId, Long userId) {
        Reminder reminder = reminderRepository.findById(reminderId)
                .orElseThrow(() -> new EntityNotFoundException(Reminder.class, "reminderId", reminderId));
        if (!reminder.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You are not allowed to access this reminder");
        }
        return reminder;
    }

    public void deleteById(Long reminderId, Long userId) {
        Reminder reminder = reminderRepository.findById(reminderId)
                .orElseThrow(() -> new EntityNotFoundException(Reminder.class, "reminderId",  reminderId));
        if (!reminder.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You are not allowed to access this reminder");
        }
        reminderRepository.deleteById(reminderId);
    }
}
