package com.example.demo.reminder.controller;

import com.example.demo.common.wrapper.ApiResponseWrapper;
import com.example.demo.reminder.model.dto.ReminderCreateDto;
import com.example.demo.reminder.model.dto.ReminderFilterDto;
import com.example.demo.reminder.model.dto.ReminderReadDto;
import com.example.demo.reminder.model.dto.ReminderUpdateDto;
import com.example.demo.reminder.service.ReminderService;
import com.example.demo.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reminder")
@RequiredArgsConstructor
public class ReminderController {

    private final ReminderService reminderService;
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponseWrapper<ReminderReadDto>> createReminder(
            @RequestBody @Valid ReminderCreateDto dto,
            Authentication authentication) {

        ReminderReadDto data = reminderService.createReminder(dto, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseWrapper<>("Reminder created successfully", data));
    }

    @GetMapping("/{reminderId}")
    public ResponseEntity<ApiResponseWrapper<ReminderReadDto>> getReminder(
            @PathVariable Long reminderId,
            Authentication authentication) {

        ReminderReadDto data =  reminderService.getReminder(reminderId, authentication);
        return ResponseEntity.ok(new ApiResponseWrapper<>("Reminder read successfully", data));
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponseWrapper<Page<ReminderReadDto>>> getReminders(
            @ModelAttribute ReminderFilterDto dto,
            Authentication authentication) {

        Page<ReminderReadDto> data =  reminderService.getReminders(dto, authentication);
        return ResponseEntity.ok(new ApiResponseWrapper<>("Reminders retrieved successfully", data));
    }

    @PutMapping("/update/{reminderId}")
    public ResponseEntity<ApiResponseWrapper<ReminderReadDto>> updateReminder(
            @PathVariable Long reminderId,
            @RequestBody @Valid ReminderUpdateDto dto,
            Authentication authentication) {

        ReminderReadDto data = reminderService.updateReminder(reminderId, dto, authentication);
        return ResponseEntity.ok(new ApiResponseWrapper<>("Reminder updated successfully", data));
    }

    @DeleteMapping("/delete/{reminderId}")
    public ResponseEntity<ApiResponseWrapper<Void>> deleteReminder(
            @PathVariable Long reminderId,
            Authentication authentication) {

        reminderService.deleteReminder(reminderId, authentication);
        return ResponseEntity.ok(new ApiResponseWrapper<>("Reminder deleted successfully", null));
    }

}
