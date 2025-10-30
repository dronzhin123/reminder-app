package com.example.demo.reminder.sender;

import com.example.demo.reminder.model.entity.Reminder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailReminderSender implements BaseReminderSender {

    @Override
    public void sendMessage(Reminder reminder) {
        log.info("[EMAIL] Напоминание {} для пользователя {} на {}",
                reminder.getId(),
                reminder.getUser().getUsername(),
                reminder.getRemindAt());
    }

}
