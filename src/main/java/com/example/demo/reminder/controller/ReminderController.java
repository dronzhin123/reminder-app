package com.example.demo.reminder.controller;

import com.example.demo.reminder.dto.ReminderUpdateDto;
import com.example.demo.user.service.UserService;
import com.example.demo.wrapper.ApiResponseWrapper;
import com.example.demo.reminder.dto.ReminderCreateDto;
import com.example.demo.reminder.dto.ReminderReadDto;
import com.example.demo.reminder.entity.Reminder;
import com.example.demo.user.entity.User;
import com.example.demo.reminder.mapper.ReminderMapper;
import com.example.demo.reminder.service.ReminderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/v1/reminder")
@RequiredArgsConstructor
public class ReminderController {

    private final ReminderService reminderService;
    private final ReminderMapper reminderMapper;
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponseWrapper<ReminderReadDto>> create(
            @RequestBody @Valid ReminderCreateDto dto,
            Authentication authentication) {

        User user = userService.findByUsername(authentication.getName());
        Reminder reminder =  reminderService.save(reminderMapper.toReminder(dto, user));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseWrapper<>("Reminder created successfully",
                        reminderMapper.toDto(reminder), null));
    }

    @GetMapping("find/{reminderId}")
    public ResponseEntity<ApiResponseWrapper<ReminderReadDto>> findById(
            @PathVariable Long reminderId,
            Authentication authentication) {

        User user = userService.findByUsername(authentication.getName());
        Reminder reminder = reminderService.findById(reminderId, user.getId());
        return ResponseEntity.ok(new ApiResponseWrapper<>("Reminder read successfully",
                reminderMapper.toDto(reminder), null));
    }

    @PutMapping("/update/{reminderId}")
    public ResponseEntity<ApiResponseWrapper<ReminderReadDto>> updateById(
            @PathVariable Long reminderId,
            @RequestBody @Valid ReminderUpdateDto dto,
            Authentication authentication) {

        User user = userService.findByUsername(authentication.getName());
        Reminder reminder = reminderService.findById(reminderId, user.getId());
        reminderMapper.update(reminder, dto);
        reminderService.save(reminder);
        return ResponseEntity.ok(new ApiResponseWrapper<>("Reminder updated successfully",
                        reminderMapper.toDto(reminder), null));
    }

    @DeleteMapping("delete/{reminderId}")
    public ResponseEntity<ApiResponseWrapper<Void>> deleteById(
            @PathVariable Long reminderId,
            Authentication authentication) {

        User user = userService.findByUsername(authentication.getName());
        reminderService.deleteById(reminderId, user.getId());
        return ResponseEntity.ok(new ApiResponseWrapper<>("Reminder deleted successfully", null, null));
    }

}
