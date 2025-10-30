package com.example.demo.reminder.specification;

import com.example.demo.reminder.model.dto.ReminderFilterDto;
import com.example.demo.reminder.model.entity.Reminder;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class ReminderSpecification {

    private static final String FORMAT = "%%%s%%";

    private static Specification<Reminder> hasUserId(Long userId) {
        return (root, query, cb) -> (userId == null) ? cb.conjunction() :
                cb.equal(root.get("userId"), userId);
    }

    private static Specification<Reminder> createdAtAfter(LocalDateTime start) {
        return (root, query, cb) -> (start == null) ? cb.conjunction() :
                cb.greaterThanOrEqualTo(root.get("createdAt"), start);
    }

    private static Specification<Reminder> createdAtBefore(LocalDateTime end) {
        return (root, query, cb) -> (end == null) ? cb.conjunction() :
                cb.lessThanOrEqualTo(root.get("createdAt"), end);
    }

    private static Specification<Reminder> remindAtAfter(LocalDateTime start) {
        return (root, query, cb) -> (start == null) ? cb.conjunction() :
                cb.greaterThanOrEqualTo(root.get("remindAt"), start);
    }

    private static Specification<Reminder> remindAtBefore(LocalDateTime end) {
        return (root, query, cb) -> (end == null) ? cb.conjunction() :
                cb.lessThanOrEqualTo(root.get("remindAt"), end);
    }

    private static Specification<Reminder> hasStatus(String status) {
        return (root, query, cb) -> (status == null) ? cb.conjunction() :
                cb.equal(root.get("status"), status);
    }

    private static Specification<Reminder> hasSender(String sender) {
        return (root, query, cb) -> (sender == null) ? cb.conjunction() :
                cb.equal(root.get("sender"), sender);
    }

    private static Specification<Reminder> containsKeyword(String keyword) {
        return (root, query, cb) -> (keyword == null) ? cb.conjunction() :
                cb.like(cb.lower(root.get("text")), FORMAT.formatted(keyword.toLowerCase()));
    }

    private static Specification<Reminder> hasTitle(String title) {
        return (root, query, cb) -> (title == null) ? cb.conjunction() :
                cb.equal(root.get("title"), title);
    }

    private static Specification<Reminder> hasIdNot(Long reminderId) {
        return (root, query, cb) -> (reminderId == null) ? cb.conjunction() :
                cb.notEqual(root.get("id"), reminderId);
    }

    public static Specification<Reminder> withFilter(ReminderFilterDto dto, Long userId) {
        return hasUserId(userId)
                .and(createdAtAfter(dto.getCreatedAtStart()))
                .and(createdAtBefore(dto.getCreatedAtEnd()))
                .and(remindAtAfter(dto.getRemindAtStart()))
                .and(remindAtBefore(dto.getRemindAtEnd()))
                .and(hasStatus(dto.getStatus() != null ? dto.getStatus().name() : null))
                .and(hasSender(dto.getSender() != null ? dto.getSender().name() : null))
                .and(containsKeyword(dto.getKeyword()));
    }

    public static Specification<Reminder> withTitle(String title, Long userId, Long reminderId) {
        return hasUserId(userId).and(hasTitle(title)).and(ReminderSpecification.hasIdNot(reminderId));
    }

}
