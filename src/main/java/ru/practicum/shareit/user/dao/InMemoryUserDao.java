package ru.practicum.shareit.user.dao;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class InMemoryUserDao implements UserDao {
    private final Map<Integer, User> users = new HashMap<>();
    private int counter = 0;

    @Override
    public User add(User user) {
        users.put(++counter, user);
        user.setId(counter);
        return user;
    }

    @Override
    public Optional<User> get(int id) {
        return Optional.ofNullable(users.get(id));
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
    public void delete(int id) {
         users.remove(id);
    }

    @Override
    public Collection<User> getAll() {
        return users.values();
    }
}
