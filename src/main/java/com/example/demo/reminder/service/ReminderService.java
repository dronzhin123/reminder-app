package com.example.demo.reminder.service;

import com.example.demo.reminder.model.dto.ReminderCreateDto;
import com.example.demo.reminder.model.dto.ReminderFilterDto;
import com.example.demo.reminder.model.dto.ReminderReadDto;
import com.example.demo.reminder.model.dto.ReminderUpdateDto;
import com.example.demo.reminder.model.entity.Reminder;
import com.example.demo.reminder.model.mapper.ReminderMapper;
import com.example.demo.reminder.repository.ReminderRepository;
import com.example.demo.reminder.specification.ReminderSpecifications;
import com.example.demo.user.model.entity.User;
import com.example.demo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ReminderService {

    private final ReminderRepository reminderRepository;
    private final ReminderMapper reminderMapper;
    private final UserService userService;

    private final Set<String> allowedSortByValues = Set.of("title", "createdAt", "remindAt");
    private final Set<String> allowedDirectionValues = Set.of("asc", "desc");

    @Transactional
    public ReminderReadDto saveReminder(ReminderCreateDto dto, User user) {;
        if (dto.title() != null && reminderRepository.existsByTitleAndUserId(dto.title(), user.getId())) {
            throw new RuntimeException("Reminder with title '" + dto.title() + "' already exists for this user");
        }
        Reminder reminder = reminderMapper.toReminder(dto, user);
        return reminderMapper.toDto(reminderRepository.save(reminder));
    }

    public ReminderReadDto getReminder(Long reminderId, User user) {
        Reminder reminder = findByIdAndUserId(reminderId, user.getId());
        return reminderMapper.toDto(reminder);
    }

    public Page<ReminderReadDto> getReminders(User user, ReminderFilterDto dto) {
        Pageable pageable = createPageRequest(dto.getPage(), dto.getSize(), dto.getSortBy(), dto.getDirection());
        Specification<Reminder> specification = ReminderSpecifications.byUser(user.getId());
        if (dto.getKeyword() != null && !dto.getKeyword().isEmpty()) {
            specification = specification.and(ReminderSpecifications.hasKeyword(dto.getKeyword()));
        }
        if (dto.getCreatedAtStart() != null) {
            specification = specification.and(ReminderSpecifications.createdAtAfter(dto.getCreatedAtStart()));
        }
        if (dto.getCreatedAtEnd() != null) {
            specification = specification.and(ReminderSpecifications.createdAtBefore(dto.getCreatedAtEnd()));
        }
        if (dto.getRemindAtStart() != null) {
            specification = specification.and(ReminderSpecifications.remindAtAfter(dto.getRemindAtStart()));
        }
        if (dto.getRemindAtEnd() != null) {
            specification = specification.and(ReminderSpecifications.remindAtBefore(dto.getRemindAtEnd()));
        }
        Page<Reminder> reminders = reminderRepository.findAll(specification, pageable);
        return reminders.map(reminderMapper::toDto);
    }

    @Transactional
    public ReminderReadDto updateReminder(Long reminderId, ReminderUpdateDto dto, User user) {
        if (dto.title() != null && reminderRepository.existsByTitleAndUserIdAndIdNot(dto.title(), user.getId(), reminderId)) {
            throw new RuntimeException("Reminder with title '" + dto.title() + "' already exists for this user");
        }
        Reminder reminder = findByIdAndUserId(reminderId, user.getId());
        reminderMapper.update(reminder, dto);
        return reminderMapper.toDto(reminderRepository.save(reminder));
    }

    @Transactional
    public void deleteReminder(Long reminderId, User user) {
        Reminder reminder = findByIdAndUserId(reminderId, user.getId());
        reminderRepository.delete(reminder);
    }

    private Reminder findByIdAndUserId(Long reminderId, Long userId) {
        return reminderRepository.findByIdAndUserId(reminderId, userId)
                .orElseThrow(() -> new RuntimeException("Reminder not found with id " + reminderId + " for this user"));
    }

    private PageRequest createPageRequest(int page, int size, String sortBy, String direction) {
        if (!allowedSortByValues.contains(sortBy)) {
            throw new RuntimeException("Invalid sortBy value: " + sortBy + ". Allowed values: " + allowedSortByValues);
        }
        if (!allowedDirectionValues.contains(direction)) {
            throw new RuntimeException("Invalid direction value: " + direction + ". Allowed values: " + allowedDirectionValues);
        }
        Sort sort = direction.equals("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        return PageRequest.of(page, size, sort);
    }

}
