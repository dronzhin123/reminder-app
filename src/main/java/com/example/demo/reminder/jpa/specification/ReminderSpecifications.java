package com.example.demo.reminder.jpa.specification;

import com.example.demo.reminder.model.dto.ReminderFilterDto;
import com.example.demo.reminder.model.entity.Reminder;
import com.example.demo.user.model.entity.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ReminderSpecifications {

    private static final String FORMAT = "%%%s%%";

    public static Specification<Reminder> getSpecification(ReminderFilterDto dto, Long userId) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("user").get("id"), userId));
            if (dto.getCreatedAtStart() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), dto.getCreatedAtStart()));
            }
            if (dto.getCreatedAtEnd() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), dto.getCreatedAtEnd()));
            }
            if (dto.getRemindAtStart() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("remindAt"), dto.getRemindAtStart()));
            }
            if (dto.getRemindAtEnd() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("remindAt"), dto.getRemindAtEnd()));
            }
            if (dto.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), dto.getStatus()));
            }
            if (dto.getSender() != null) {
                predicates.add(cb.equal(root.get("sender"), dto.getSender()));
            }
            if (dto.getKeyword() != null && !dto.getKeyword().isBlank()) {
                String pattern = String.format(FORMAT, dto.getKeyword().toLowerCase());
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("title")), pattern),
                        cb.like(cb.lower(root.get("description")), pattern)
                ));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
