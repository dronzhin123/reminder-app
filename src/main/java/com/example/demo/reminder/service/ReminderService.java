package com.example.demo.reminder.service;

import com.example.demo.reminder.event.dto.ReminderEventDto;
import com.example.demo.reminder.exception.NoContactForSenderException;
import com.example.demo.reminder.exception.ReminderAlreadyExistsException;
import com.example.demo.reminder.exception.ReminderNotFoundException;
import com.example.demo.reminder.model.dto.ReminderCreateDto;
import com.example.demo.reminder.model.dto.ReminderFilterDto;
import com.example.demo.reminder.model.dto.ReminderReadDto;
import com.example.demo.reminder.model.dto.ReminderUpdateDto;
import com.example.demo.reminder.model.entity.Reminder;
import com.example.demo.reminder.model.mapper.ReminderMapper;
import com.example.demo.reminder.repository.ReminderRepository;
import com.example.demo.reminder.specification.ReminderSpecification;
import com.example.demo.user.model.entity.User;
import com.example.demo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReminderService {

    private final ReminderRepository reminderRepository;
    private final ReminderMapper reminderMapper;
    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;

    public Reminder getReminder(Long reminderId, Long userId) {
        return reminderRepository.findByIdAndUserId(reminderId, userId).orElseThrow(() -> new ReminderNotFoundException(reminderId));
    }

    public Reminder getReminder(Long reminderId) {
        return reminderRepository.findWithUserById(reminderId).orElseThrow(() -> new ReminderNotFoundException(reminderId));
    }

    public ReminderReadDto getReminder(Long reminderId, Authentication authentication) {
        Long userId = userService.getCurrentUserId(authentication);
        Reminder reminder = getReminder(reminderId, userId);
        return reminderMapper.toDto(reminder);
    }

    public Page<ReminderReadDto> getReminders(ReminderFilterDto dto, Authentication authentication) {
        Long userId = userService.getCurrentUserId(authentication);
        Specification<Reminder> specification = ReminderSpecification.withFilter(dto, userId);
        Sort sort = createSort(dto.getDirection(), dto.getSortField());
        PageRequest pageRequest = PageRequest.of(dto.getPage(), dto.getSize(), sort);
        return reminderRepository.findAll(specification, pageRequest).map(reminderMapper::toDto);
    }

    @Transactional
    public ReminderReadDto createReminder(ReminderCreateDto dto, Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        validateSender(dto, user);
        validateTitle(dto.title(), user.getId(), null);
        Reminder reminder = reminderMapper.toReminder(dto, user);
        reminder = reminderRepository.save(reminder);
        eventPublisher.publishEvent(new ReminderEventDto(reminder, ReminderEventDto.Type.CREATED));
        return reminderMapper.toDto(reminder);
    }

    @Transactional
    public ReminderReadDto updateReminder(Long reminderId, ReminderUpdateDto dto, Authentication authentication) {
        Long userId = userService.getCurrentUserId(authentication);
        validateTitle(dto.title(), userId, reminderId);
        Reminder reminder = getReminder(reminderId, userId);
        reminderMapper.update(reminder, dto);
        eventPublisher.publishEvent(new ReminderEventDto(reminder, ReminderEventDto.Type.UPDATED));
        return reminderMapper.toDto(reminder);
    }

    @Transactional
    public void deleteReminder(Long reminderId, Authentication authentication) {
        Long userId = userService.getCurrentUserId(authentication);
        Reminder reminder = getReminder(reminderId, userId);
        eventPublisher.publishEvent(new ReminderEventDto(reminder, ReminderEventDto.Type.DELETED));
        reminderRepository.delete(reminder);
    }

    @Transactional
    public void updateStatus(Reminder reminder, Reminder.Status status) {
        reminder.setStatus(status);
    }

    private Sort createSort(String direction, String sortField) {
        return Sort.by(Sort.Direction.fromString(direction), sortField);
    }

    private void validateSender(ReminderCreateDto dto, User user) {
        String contact = switch (dto.sender()) {
            case EMAIL -> user.getEmail();
            case TELEGRAM -> user.getTelegram();
        };
        if (contact == null) {
            throw new NoContactForSenderException(dto.sender().name());
        }
    }

    private void validateTitle(String title, Long userId, Long reminderId) {
        Specification<Reminder> specification = ReminderSpecification.withTitle(title, userId, reminderId);
        if (reminderRepository.count(specification) > 0) {
            throw new ReminderAlreadyExistsException("title", title);
        }
    }

}
