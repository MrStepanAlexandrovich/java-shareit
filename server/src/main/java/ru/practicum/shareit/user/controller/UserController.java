package ru.practicum.shareit.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserEditDto;
import ru.practicum.shareit.user.service.UserService;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable int userId) {
        return new ResponseEntity<>(
                userService.get(userId),
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserCreateDto userCreateDto) {
        return new ResponseEntity<>(
                userService.add(userCreateDto),
                HttpStatus.OK
        );
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserDto> editUser(
            @PathVariable int userId,
            @RequestBody UserEditDto userEditDto
    ) {
        return new ResponseEntity<>(
                userService.edit(userId, userEditDto),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{userId}")
    public void removeUser(@PathVariable int userId) {
        userService.delete(userId);
    }
}
