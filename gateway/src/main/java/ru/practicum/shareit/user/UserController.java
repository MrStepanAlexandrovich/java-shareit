package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserEditDto;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserClient userClient;

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUser(@PathVariable @Positive int userId) {
        return userClient.getUser(userId);
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserCreateDto userCreateDto) {
        return userClient.createUser(userCreateDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> editUser(
            @PathVariable @Positive int userId,
            @RequestBody @Valid UserEditDto userEditDto
    ) {
        return userClient.editUser(userId, userEditDto);
    }

    @DeleteMapping("/{userId}")
    public void removeUser(@Positive @PathVariable int userId) {
        userClient.removeUser(userId);
    }
}
