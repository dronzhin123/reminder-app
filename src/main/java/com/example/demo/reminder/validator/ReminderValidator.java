package com.example.demo.reminder.validator;

import com.example.demo.reminder.jpa.repository.ReminderRepository;
import com.example.demo.reminder.model.dto.ReminderCreateDto;
import com.example.demo.user.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReminderValidator {

    private final ReminderRepository reminderRepository;

    public void validateSender(ReminderCreateDto dto, User user) {
        String contact = switch (dto.sender()) {
            case EMAIL -> user.getEmail();
            case TELEGRAM -> user.getTelegram();
        };
        if (contact == null) {
            throw new RuntimeException("User has no %s for sending reminder".formatted(dto.sender()));
        }
    }

    public void validateTitle(String title, Long userId, Long excludeId) {
        boolean exists = (excludeId == null) ?
                reminderRepository.existsByTitleAndUserId(title, userId) :
                reminderRepository.existsByTitleAndUserIdAndIdNot(title, userId, excludeId);
        if (exists) {
            throw new RuntimeException("Reminder with title '%s' already exists for this user".formatted(title));
        }
    }

}
