package com.example.demo.reminder.scheduler;

import com.example.demo.reminder.job.ReminderJob;
import com.example.demo.reminder.model.entity.Reminder;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class ReminderScheduler {

    private final Scheduler scheduler;

    public void schedule(Reminder reminder) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(ReminderJob.class)
                .withIdentity("reminderJob-%d".formatted(reminder.getId()))
                .usingJobData("reminderId", reminder.getId())
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("reminderTrigger-%d".formatted(reminder.getId()))
                .startAt(Date.from(reminder.getRemindAt().atZone(ZoneId.systemDefault()).toInstant()))
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
    }

    public void reschedule(Reminder reminder) throws SchedulerException {
        delete(reminder.getId());
        schedule(reminder);
    }

    public void delete(Long reminderId) throws SchedulerException {
        scheduler.deleteJob(new JobKey("reminderJob-%d".formatted(reminderId)));
    }

}
