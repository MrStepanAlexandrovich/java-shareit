package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
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
    public ResponseEntity getUser(@PathVariable int userId) {
        return new ResponseEntity(
                UserMapper.toUserDto(userService.get(userId)),
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity createUser(@RequestBody @Valid UserDto userDto) {
        return new ResponseEntity(
                UserMapper.toUserDto(userService.add(UserMapper.toUser(userDto))),
                HttpStatus.OK
        );
    }

    @PatchMapping("/{userId}")
    public ResponseEntity editUser(
            @PathVariable int userId,
            @RequestBody UserDto userDto
    ) {
        return new ResponseEntity(
                UserMapper.toUserDto(userService.edit(
                                userId,
                                UserMapper.toUser(userDto)
                        )
                ),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity removeUser(@PathVariable int userId) {
        return new ResponseEntity(
                UserMapper.toUserDto(userService.delete(userId)),
                HttpStatus.OK
        );
    }
}
