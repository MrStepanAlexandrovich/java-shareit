package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserEditDto;

public interface UserService {
    UserDto add(UserCreateDto userDto);

    UserDto get(int id);

    UserDto edit(int id, UserEditDto userDto);

    void delete(int id);
}
