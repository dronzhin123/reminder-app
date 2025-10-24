package com.example.demo.reminder.logic.controller;

import com.example.demo.reminder.model.dto.ReminderUpdateDto;
import com.example.demo.wrapper.ApiResponseWrapper;
import com.example.demo.reminder.model.dto.ReminderCreateDto;
import com.example.demo.reminder.model.dto.ReminderReadDto;
import com.example.demo.user.model.entity.User;
import com.example.demo.reminder.logic.service.ReminderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reminder")
@RequiredArgsConstructor
public class ReminderController {

    private final ReminderService reminderService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponseWrapper<ReminderReadDto>> createReminder(
            @RequestBody @Valid ReminderCreateDto dto,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();
        ReminderReadDto data = reminderService.saveReminder(dto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseWrapper<>("Reminder created successfully", data));
    }

    @GetMapping("/find/{reminderId}")
    public ResponseEntity<ApiResponseWrapper<ReminderReadDto>> getReminder(
            @PathVariable Long reminderId,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();
        ReminderReadDto data =  reminderService.getReminder(reminderId, user);
        return ResponseEntity.ok(new ApiResponseWrapper<>("Reminder read successfully", data));
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponseWrapper<Page<ReminderReadDto>>> getReminders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();
        Page<ReminderReadDto> data =  reminderService.getReminders(user, PageRequest.of(page, size));
        return ResponseEntity.ok(new ApiResponseWrapper<>("Reminders retrieved successfully", data));
    }

    @PutMapping("/update/{reminderId}")
    public ResponseEntity<ApiResponseWrapper<ReminderReadDto>> updateReminder(
            @PathVariable Long reminderId,
            @RequestBody @Valid ReminderUpdateDto dto,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();
        ReminderReadDto data = reminderService.updateReminder(reminderId, dto, user);
        return ResponseEntity.ok(new ApiResponseWrapper<>("Reminder updated successfully", data));
    }

    @DeleteMapping("/delete/{reminderId}")
    public ResponseEntity<ApiResponseWrapper<Void>> deleteReminder(
            @PathVariable Long reminderId,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();
        reminderService.deleteReminder(reminderId, user);
        return ResponseEntity.ok(new ApiResponseWrapper<>("Reminder deleted successfully", null));
    }

}
