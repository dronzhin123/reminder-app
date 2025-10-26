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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ReminderService {

    private final ReminderRepository reminderRepository;
    private final ReminderMapper reminderMapper;

    private final Set<String> allowedSortByValues = Set.of("title", "reminderDateTime");

    private final Set<String> allowedDirectionValues = Set.of("asc", "desc");

    public ReminderReadDto saveReminder(ReminderCreateDto dto, User user) {
        if (dto.title() != null && reminderRepository.existsByTitleAndUserId(dto.title(), user.getId())) {
            throw new EntityAlreadyExistsException(Reminder.class, "title", dto.title());
        }
        Reminder reminder = reminderMapper.toReminder(dto, user);
        return reminderMapper.toDto(reminderRepository.save(reminder));
    }

    public ReminderReadDto getReminder(Long reminderId, User user) {
        Reminder reminder = findByIdAndUserId(reminderId, user.getId());
        return reminderMapper.toDto(reminder);
    }

    public Page<ReminderReadDto> getReminders(User user, int page, int size, String sortBy, String direction, String keyword) {
        PageRequest pageRequest = createPageRequest(page, size, sortBy, direction);
        if (keyword == null || keyword.isEmpty()) {
            return reminderRepository.findByUserId(user.getId(), pageRequest)
                    .map(reminderMapper::toDto);
        }
        return reminderRepository.findByKeyword(user.getId(), keyword, pageRequest)
                .map(reminderMapper::toDto);
    }

    public ReminderReadDto updateReminder(Long reminderId, ReminderUpdateDto dto, User user) {
        if (dto.title() != null && reminderRepository.existsByTitleAndUserIdAndIdNot(dto.title(), user.getId(), reminderId)) {
            throw new EntityAlreadyExistsException(Reminder.class, "title", dto.title());
        }
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

    private PageRequest createPageRequest(int page, int size, String sortBy, String direction) {
        if (!allowedSortByValues.contains(sortBy)) {
            throw new IllegalArgumentException("Illegal sortBy: " + sortBy + ", allowed values: " + allowedSortByValues);
        }
        if (!allowedDirectionValues.contains(direction)) {
            throw new IllegalArgumentException("Illegal direction: " + direction + ", allowed values: " + allowedDirectionValues);
        }
        Sort sort = direction.equals("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        return PageRequest.of(page, size, sort);
    }

}
