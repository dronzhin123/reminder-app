package com.example.demo.reminder.logic.service;

import com.example.demo.exception.EntityAlreadyExistsException;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.reminder.model.dto.ReminderCreateDto;
import com.example.demo.reminder.model.dto.ReminderReadDto;
import com.example.demo.reminder.model.dto.ReminderUpdateDto;
import com.example.demo.reminder.model.entity.Reminder;
import com.example.demo.reminder.model.mapper.ReminderMapper;
import com.example.demo.reminder.logic.repository.ReminderRepository;
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
        if (reminderRepository.existsByTitleAndUserId(dto.title(), user.getId())) {
            throw new EntityAlreadyExistsException(Reminder.class, "title", dto.title());
        }
        Reminder reminder = reminderMapper.toReminder(dto, user);
        return reminderMapper.toDto(reminderRepository.save(reminder));
    }

    public ReminderReadDto getReminder(Long reminderId, User user) {
        Reminder reminder = reminderRepository.findByIdAndUserId(reminderId, user.getId())
                .orElseThrow(() -> new EntityNotFoundException(Reminder.class, "id", reminderId));
        return reminderMapper.toDto(reminder);
    }

    public Page<ReminderReadDto> getReminders(User user, Pageable pageable) {
        return reminderRepository.findByUserId(user.getId(), pageable).map(reminderMapper::toDto);
    }

    public ReminderReadDto updateReminder(Long reminderId, ReminderUpdateDto dto, User user) {
        Reminder reminder = reminderRepository.findByIdAndUserId(reminderId, user.getId())
                .orElseThrow(() -> new EntityNotFoundException(Reminder.class, "id", reminderId));
        if (!reminder.getTitle().equals(dto.title()) && reminderRepository.existsByTitleAndUserId(dto.title(), user.getId())) {
            throw new EntityAlreadyExistsException(Reminder.class, "title", dto.title());
        }
        reminderMapper.update(reminder, dto);
        return reminderMapper.toDto(reminderRepository.save(reminder));
    }

    public void deleteReminder(Long reminderId, User user) {
        Reminder reminder = reminderRepository.findByIdAndUserId(reminderId, user.getId())
                .orElseThrow(() -> new EntityNotFoundException(Reminder.class, "id", reminderId));
        reminderRepository.delete(reminder);
    }

}
