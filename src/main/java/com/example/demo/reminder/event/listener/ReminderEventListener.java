package com.example.demo.reminder.event.listener;

import com.example.demo.reminder.event.dto.ReminderEventDto;
import com.example.demo.reminder.scheduler.ReminderScheduler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReminderEventListener {

    private final ReminderScheduler reminderScheduler;

    @TransactionalEventListener
    public void handleReminderEvent(ReminderEventDto dto) {
        try {
            switch (dto.type()) {
                case CREATED -> reminderScheduler.schedule(dto.reminder());
                case UPDATED -> reminderScheduler.reschedule(dto.reminder());
                case DELETED -> reminderScheduler.delete(dto.reminder().getId());
            }
        } catch (SchedulerException exception) {
            log.error("Failed to handle reminder event [{}] for reminder {}", dto.type(), dto.reminder().getId(), exception);

        }
    }

}