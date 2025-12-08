package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.model.User;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    @Override
    public User add(User user) {
        return userDao.add(user);
    }

    @Override
    public User get(int id) {
        return userDao.get(id);
    }

    @Override
    public User edit(int id, User user) {
        return userDao.edit(id, user);
    }

    @Override
    public User delete(int id) {
        return userDao.delete(id);
    }
}
