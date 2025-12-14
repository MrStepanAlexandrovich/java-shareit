package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.model.User;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    @Override
    public User add(User user) {
        if (isEmailUnique(user.getEmail())) {
            return userDao.add(user);
        } else {
            throw new ConflictException("Email is not unique");
        }
    }

    @Override
    public User get(int id) {
        return userDao.get(id);
    }

    @Override
    public User edit(int id, User user) {
        if (isEmailUnique(user.getEmail())) {
            return userDao.edit(id, user);
        } else {
            throw new ConflictException("Email is not unique");
        }
    }

    @Override
    public User delete(int id) {
        return userDao.delete(id);
    }

    private boolean isEmailUnique(String email) {
        if (!userDao.getAll().isEmpty()) {
            return userDao.getAll()
                    .stream()
                    .map(User::getEmail)
                    .filter(email1 -> email1 != null)
                    .noneMatch(email2 -> email2.equals(email));
        } else {
            return true;
        }

    }
}
