package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

public interface UserService {
    User add(User user);

    User get(int id);

    User edit(int id, User user);

    void delete(int id);
}
