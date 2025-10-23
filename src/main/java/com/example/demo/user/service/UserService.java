package com.example.demo.user.service;

import com.example.demo.exception.EntityAlreadyExistsException;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.exception.PasswordsNotMatchException;
import com.example.demo.user.entity.User;
import com.example.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User save(User user, String confirmPassword) {
        String username = user.getUsername();
        if (userRepository.existsByUsername(username)) {
            throw new EntityAlreadyExistsException(User.class, "username", username);
        }
        String email = user.getEmail();
        if (userRepository.existsByEmail(email)) {
            throw new EntityAlreadyExistsException(User.class, "email", email);
        }
        String password = user.getPassword();
        if(!password.equals(confirmPassword)) {
            throw new PasswordsNotMatchException();
        }
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(User.class, "username", username));
    }

}
