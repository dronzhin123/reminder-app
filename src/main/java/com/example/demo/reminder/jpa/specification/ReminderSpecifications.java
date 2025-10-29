package com.example.demo.reminder.jpa.specification;

import com.example.demo.reminder.model.entity.Reminder;
import org.springframework.data.jpa.domain.Specification;

public class ReminderSpecifications {

    private static final String FORMAT = "%%%s%%";

    public static Specification<Reminder> hasUserId(Long userId) {
        return (root, query, cb) -> cb.equal(root.get("user").get("id"), userId);
    }

    public static Specification<Reminder> createdAtAfter(java.time.LocalDateTime start) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("createdAt"), start);
    }

    public static Specification<Reminder> createdAtBefore(java.time.LocalDateTime end) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("createdAt"), end);
    }

    public static Specification<Reminder> remindAtAfter(java.time.LocalDateTime start) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("remindAt"), start);
    }

    public static Specification<Reminder> remindAtBefore(java.time.LocalDateTime end) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("remindAt"), end);
    }

    public static Specification<Reminder> hasStatus(String status) {
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    public static Specification<Reminder> hasSender(String sender) {
        return (root, query, cb) -> cb.equal(root.get("sender"), sender);
    }

    public static Specification<Reminder> containsKeyword(String keyword) {
        String pattern = String.format(FORMAT, keyword.toLowerCase());
        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("title")), pattern),
                cb.like(cb.lower(root.get("description")), pattern)
        );
    }

}
