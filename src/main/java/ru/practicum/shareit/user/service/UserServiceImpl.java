package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User add(User user) {
        if (isEmailUnique(user.getEmail())) {
            return userRepository.save(user);
        } else {
            throw new ConflictException("Email is not unique");
        }
    }

    @Override
    public User get(int id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new NotFoundException("User with id = " + id + " wasn't found");
        }
    }

    @Override
    public User edit(int id, User user) {
        user.setId(id);
        if (isEmailUnique(user.getEmail())) {
            return  userRepository.save(user);
        } else {
            throw new ConflictException("Email is not unique");
        }
    }

    @Override
    public void delete(int id) {
        userRepository.deleteById(id);
    }

    private boolean isEmailUnique(String email) {
        return userRepository.findAll()
                .stream()
                .map(User::getEmail)
                .filter(email1 -> email1 != null)
                .noneMatch(email2 -> email2.equals(email));
    }
}
