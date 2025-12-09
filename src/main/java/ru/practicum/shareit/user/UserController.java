package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable int userId) {
        return UserMapper.toUserDto(userService.get(userId));
    }

    @PostMapping
    public UserDto createUser(@RequestBody @Valid UserDto userDto) {
        return UserMapper.toUserDto(
                userService.add(
                        UserMapper.toUser(userDto)
                )
        );
    }

    @PatchMapping("/{userId}")
    public UserDto editUser(
            @PathVariable int userId,
            @RequestBody UserDto userDto
    ) {
        return UserMapper.toUserDto(
                userService.edit(
                        userId,
                        UserMapper.toUser(userDto)
                )
        );
    }

    @DeleteMapping("/{userId}")
    public UserDto removeUser(@PathVariable int userId) {
        return UserMapper.toUserDto(userService.delete(userId));
    }
}
