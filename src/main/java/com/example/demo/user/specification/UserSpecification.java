package com.example.demo.user.specification;

import com.example.demo.user.model.entity.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {

    private static Specification<User> hasUsername(String username) {
        return (root, query, cb) -> (username == null) ? cb.conjunction() : cb.equal(root.get("username"), username);
    }

    private static Specification<User> hasEmail(String email) {
        return (root, query, cb) -> (email == null) ? cb.conjunction() : cb.equal(root.get("email"), email);
    }

    private static Specification<User> hasTelegram(String telegram) {
        return (root, query, cb) -> (telegram == null) ? cb.conjunction() : cb.equal(root.get("telegram"), telegram);
    }

    private static Specification<User> hasIdNot(Long userId) {
        return (root, query, cb) -> (userId == null) ? cb.conjunction() : cb.notEqual(root.get("id"), userId);
    }

    public static Specification<User> withUsernameOrEmailOrTelegram(String username, String email, String telegram, Long userId) {
        return UserSpecification.hasUsername(username).or(hasEmail(email)).or(hasTelegram(telegram)).and(hasIdNot(userId));
    }

}

