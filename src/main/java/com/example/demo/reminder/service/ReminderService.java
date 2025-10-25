package com.example.demo.reminder.service;

import com.example.demo.exception.EntityAlreadyExistsException;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.reminder.model.dto.ReminderCreateDto;
import com.example.demo.reminder.model.dto.ReminderReadDto;
import com.example.demo.reminder.model.dto.ReminderUpdateDto;
import com.example.demo.reminder.model.entity.Reminder;
import com.example.demo.reminder.model.mapper.ReminderMapper;
import com.example.demo.reminder.repository.ReminderRepository;
import com.example.demo.user.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReminderService {

    private final ReminderRepository reminderRepository;
    private final ReminderMapper reminderMapper;

    public ReminderReadDto saveReminder(ReminderCreateDto dto, User user) {
        validateUniqueTitle(dto.title(), user.getId());
        Reminder reminder = reminderMapper.toReminder(dto, user);
        return reminderMapper.toDto(reminderRepository.save(reminder));
    }

    public ReminderReadDto getReminder(Long reminderId, User user) {
        Reminder reminder = findByIdAndUserId(reminderId, user.getId());
        return reminderMapper.toDto(reminder);
    }

    public Page<ReminderReadDto> getReminders(User user, Pageable pageable) {
        return reminderRepository.findByUserId(user.getId(), pageable).map(reminderMapper::toDto);
    }

    public ReminderReadDto updateReminder(Long reminderId, ReminderUpdateDto dto, User user) {
        validateUniqueTitle(dto.title(), user.getId(), reminderId);
        Reminder reminder = findByIdAndUserId(reminderId, user.getId());
        reminderMapper.update(reminder, dto);
        return reminderMapper.toDto(reminderRepository.save(reminder));
    }

    public void deleteReminder(Long reminderId, User user) {
        Reminder reminder = findByIdAndUserId(reminderId, user.getId());
        reminderRepository.delete(reminder);
    }

    private Reminder findByIdAndUserId(Long reminderId, Long userId) {
        return reminderRepository.findByIdAndUserId(reminderId, userId)
                .orElseThrow(() -> new EntityNotFoundException(Reminder.class, "id", reminderId));
    }

    private void validateUniqueTitle(String title, Long userId) {
        if (title != null && reminderRepository.existsByTitleAndUserId(title, userId)) {
            throw new EntityAlreadyExistsException(Reminder.class, "title", title);
        }
    }

    private void validateUniqueTitle(String title, Long userId, Long reminderId) {
        if (title != null && reminderRepository.existsByTitleAndUserIdAndIdNot(title, userId, reminderId)) {
            throw new EntityAlreadyExistsException(Reminder.class, "title", title);
        }
    }

}
