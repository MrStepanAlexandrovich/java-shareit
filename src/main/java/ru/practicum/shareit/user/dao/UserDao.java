package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserDao {
    User add(User user);

    Optional<User> get(int id);

    User edit(int id, User user);

    void delete(int id);

    Collection<User> getAll();
}
