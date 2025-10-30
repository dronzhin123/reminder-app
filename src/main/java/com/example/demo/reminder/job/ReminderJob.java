package com.example.demo.reminder.job;

import com.example.demo.reminder.model.entity.Reminder;
import com.example.demo.reminder.sender.BaseReminderSender;
import com.example.demo.reminder.sender.EmailReminderSender;
import com.example.demo.reminder.sender.TelegramReminderSender;
import com.example.demo.reminder.service.ReminderService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ReminderJob implements Job {

    private final EmailReminderSender emailReminderSender;
    private final TelegramReminderSender telegramReminderSender;
    private final ReminderService reminderService;

    private final Map<Reminder.Sender, BaseReminderSender> senders;

    @PostConstruct
    private void init() {
        senders.put(Reminder.Sender.EMAIL, emailReminderSender);
        senders.put(Reminder.Sender.TELEGRAM, telegramReminderSender);
    }

    @Override
    public void execute(JobExecutionContext context) {
        Long reminderId = context.getMergedJobDataMap().getLong("reminderId");
        Reminder reminder = reminderService.findById(reminderId);

        try {
            senders.get(reminder.getSender()).sendMessage(reminder);
            reminderService.updateStatus(reminder, Reminder.Status.SENT);
        } catch (Exception e) {
            reminderService.updateStatus(reminder, Reminder.Status.ERROR);
        }
    }

}