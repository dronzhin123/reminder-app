package com.example.demo.reminder.jpa.specification;

import com.example.demo.reminder.model.entity.Reminder;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class ReminderSpecifications {

    public static Specification<Reminder> byUser(Long userId) {
        return (root, query, cb) -> cb.equal(root.get("user").get("id"), userId);
    }

    public static Specification<Reminder> hasKeyword(String keyword) {
        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("title")), "%" + keyword.toLowerCase() + "%"),
                cb.like(cb.lower(root.get("description")), "%" + keyword.toLowerCase() + "%")
        );
    }

    public static Specification<Reminder> createdAtAfter(LocalDateTime dateTime) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("createdAt"), dateTime);
    }

    public static Specification<Reminder> createdAtBefore(LocalDateTime dateTime) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("createdAt"), dateTime);
    }

    public static Specification<Reminder> remindAtAfter(LocalDateTime dateTime) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("remindAt"), dateTime);
    }

    public static Specification<Reminder> remindAtBefore(LocalDateTime dateTime) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("remindAt"), dateTime);
    }

}
