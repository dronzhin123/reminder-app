package com.example.demo.reminder.service;

import com.example.demo.reminder.event.dto.ReminderEventDto;
import com.example.demo.exception.reminder.NoContactForSenderException;
import com.example.demo.exception.reminder.ReminderAlreadyExistsException;
import com.example.demo.exception.reminder.ReminderNotFoundException;
import com.example.demo.reminder.jpa.repository.ReminderRepository;
import com.example.demo.reminder.jpa.specification.ReminderSpecifications;
import com.example.demo.reminder.model.dto.ReminderCreateDto;
import com.example.demo.reminder.model.dto.ReminderFilterDto;
import com.example.demo.reminder.model.dto.ReminderReadDto;
import com.example.demo.reminder.model.dto.ReminderUpdateDto;
import com.example.demo.reminder.model.entity.Reminder;
import com.example.demo.reminder.model.mapper.ReminderMapper;
import com.example.demo.user.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReminderService {

    private final ReminderRepository reminderRepository;
    private final ReminderMapper reminderMapper;
    private final ReminderValidator reminderValidator;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public ReminderReadDto createReminder(ReminderCreateDto dto, User user) {
        reminderValidator.validateSender(dto, user);
        reminderValidator.validateTitle(dto.title(), user.getId(), null);
        Reminder reminder = reminderMapper.toReminder(dto, user);
        reminder = reminderRepository.save(reminder);
        eventPublisher.publishEvent(new ReminderEventDto(reminder, ReminderEventDto.Type.CREATED));
        return reminderMapper.toDto(reminder);
    }

    public ReminderReadDto getReminder(Long reminderId, User user) {
        Reminder reminder = findByIdAndUserId(reminderId, user.getId());
        return reminderMapper.toDto(reminder);
    }

    public Page<ReminderReadDto> getReminders(ReminderFilterDto dto, User user) {
        Specification<Reminder> specification = ReminderSpecifications.getSpecification(dto, user.getId());
        Sort sort = Sort.by(dto.getDirection(), dto.getSortField().value);
        PageRequest pageRequest = PageRequest.of(dto.getPage(), dto.getSize(), sort);
        return reminderRepository.findAll(specification, pageRequest).map(reminderMapper::toDto);
    }

    @Transactional
    public ReminderReadDto updateReminder(Long reminderId, ReminderUpdateDto dto, User user) {
        reminderValidator.validateTitle(dto.title(), user.getId(), reminderId);
        Reminder reminder = findByIdAndUserId(reminderId, user.getId());
        reminderMapper.update(reminder, dto);
        reminder = reminderRepository.save(reminder);
        eventPublisher.publishEvent(new ReminderEventDto(reminder, ReminderEventDto.Type.UPDATED));
        return reminderMapper.toDto(reminder);
    }

    @Transactional
    public void deleteReminder(Long reminderId, User user) {
        Reminder reminder = findByIdAndUserId(reminderId, user.getId());
        eventPublisher.publishEvent(new ReminderEventDto(reminder, ReminderEventDto.Type.DELETED));
        reminderRepository.delete(reminder);
    }

    public Reminder findWithUserById(Long reminderId) {
        return reminderRepository.findWithUserById(reminderId).orElseThrow(() -> new ReminderNotFoundException(reminderId));
    }

    @Transactional
    public void updateStatus(Reminder reminder, Reminder.Status status) {
        reminder.setStatus(status);
        reminderRepository.save(reminder);
    }

    private Reminder findByIdAndUserId(Long reminderId, Long userId) {
        return reminderRepository.findByIdAndUserId(reminderId, userId).orElseThrow(() -> new ReminderNotFoundException(reminderId));
    }

    private class ReminderValidator {

        public void validateSender(ReminderCreateDto dto, User user) {
            String contact = switch (dto.sender()) {
                case EMAIL -> user.getEmail();
                case TELEGRAM -> user.getTelegram();
            };
            if (contact == null) {
                throw new NoContactForSenderException(dto.sender().name());
            }
        }

        public void validateTitle(String title, Long userId, Long excludeId) {
            boolean exists = (excludeId == null) ?
                    reminderRepository.existsByTitleAndUserId(title, userId) :
                    reminderRepository.existsByTitleAndUserIdAndIdNot(title, userId, excludeId);
            if (exists) {
                throw new ReminderAlreadyExistsException("title", title);
            }
        }

    }

}
