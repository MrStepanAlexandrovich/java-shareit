package ru.practicum.shareit.user.dao;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserDao implements UserDao {
    private Map<Integer, User> users = new HashMap<>();
    private static int counter = 0;

    @Override
    public User add(User user) {
        users.put(++counter, user);
        user.setId(counter);
        return user;
    }

    @Override
    public User get(int id) {
        return users.get(id) != null ? users.get(id) : null;
    }

    @Override
    public User edit(int id, User user) {
        User oldUser = users.get(id);
        if (user.getName() != null) {
            oldUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            oldUser.setEmail(user.getEmail());
        }

        return oldUser;
    }

    @Override
    public User delete(int id) {
        User user = users.get(id);

        if (users.get(id) != null) {
            users.remove(id);
        }

        return user;
    }

    @Override
    public Collection<User> getAll() {
        return users.values();
    }
}
