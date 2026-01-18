package ru.practicum.shareit.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserEditDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto add(UserCreateDto userDto) {
        log.info("Creating user. Name = {}, email = {}", userDto.getName(), userDto.getEmail());

        User user = UserMapper.toUser(userDto);

        if (isEmailUnique(user.getEmail())) {
            log.info("Email {} is unique", userDto.getEmail());

            User user1 = userRepository.save(user);

            log.info("User was saved. ID = {}, name = {}, email = {}", user1.getId(), userDto.getName(),
                    userDto.getEmail());

            return UserMapper.toUserDto(user1);
        } else {
            log.warn("Email {} is not unique", userDto.getEmail());

            throw new ConflictException("Email is not unique");
        }
    }

    @Override
    public UserDto get(int id) {
        log.info("Getting user with id = {}", id);

        User user = userRepository.findById(id).orElseThrow(() -> {
            log.warn("User with id = {} wasn't found", id);

            return new NotFoundException("User with id = " + id + " wasn't found");
        });

        log.info("User with id = {} was found", id);

        return UserMapper.toUserDto(user);
    }

    @Transactional
    @Override
    public UserDto edit(int id, UserEditDto userEditDto) {
        log.info("Editing user with id = {}. Email = {}, name = {}", id, userEditDto.getEmail(), userEditDto.getName());

        User user = UserMapper.toUser(userEditDto);

        user.setId(id);
        User oldUser = userRepository.findById(id).orElseThrow(() -> {
                    log.warn("User with ID = {} wasn't found", id);

                    return new NotFoundException("User with ID = " + id + " wasn't found");
                }
        );

        if (user.getName() == null) {
            user.setName(oldUser.getName());
        } else if (user.getEmail() == null) {
            user.setEmail(oldUser.getEmail());
        }

        User user1 = userRepository.save(user);

        log.info("User with id = {} was updated. New email = {}, new name = {}", id, user1.getEmail(),
                user1.getName());

        return UserMapper.toUserDto(user1);
    }

    @Transactional
    @Override
    public void delete(int id) {
        log.info("Deleting user with id = {}", id);
        userRepository.deleteById(id);
        log.info("User with id = {} was deleted", id);
    }

    private boolean isEmailUnique(String email) {
        return userRepository.findAll()
                .stream()
                .map(User::getEmail)
                .filter(Objects::nonNull)
                .noneMatch(email2 -> email2.equals(email));
    }
}
