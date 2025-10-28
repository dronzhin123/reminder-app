package com.example.demo.reminder.sender;

import com.example.demo.reminder.model.entity.Reminder;

public interface BaseReminderSender {
    void sendMassage(Reminder reminder);
}
