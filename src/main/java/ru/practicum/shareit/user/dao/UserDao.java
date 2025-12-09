package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserDao {
    User add(User user);

    User get(int id);

    User edit(int id, User user);

    User delete(int id);

    Collection<User> getAll();
}
