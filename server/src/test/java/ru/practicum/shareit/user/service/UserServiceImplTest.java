package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserEditDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User(1, "John", "john@mail.com");
    }

    @Test
    public void addSuccess() {
        UserCreateDto create = new UserCreateDto(0, "John", "john@mail.com");
        when(userRepository.findAll()).thenReturn(List.of());
        when(userRepository.save(any())).thenAnswer(i -> {
            User u = i.getArgument(0);
            u.setId(1);
            return u;
        });

        UserDto res = userService.add(create);
        assertEquals(1, res.getId());
        assertEquals("John", res.getName());
        verify(userRepository).save(userCaptor.capture());
    }

    @Test
    public void addConflict() {
        UserCreateDto create = new UserCreateDto(0, "John", "john@mail.com");
        when(userRepository.findAll()).thenReturn(List.of(user));

        assertThrows(ConflictException.class, () -> userService.add(create));
    }

    @Test
    public void getSuccess() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        UserDto dto = userService.get(1);
        assertEquals("John", dto.getName());
    }

    @Test
    public void getNotFound() {
        when(userRepository.findById(2)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.get(2));
    }

    @Test
    public void editSuccess() {
        UserEditDto edit = new UserEditDto();
        edit.setId(1);
        edit.setName(null);
        edit.setEmail("new@mail.com");
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        UserDto res = userService.edit(1, edit);
        assertEquals("John", res.getName());
        assertEquals("new@mail.com", res.getEmail());
    }

    @Test
    public void deleteInvokesRepository() {
        doNothing().when(userRepository).deleteById(1);
        userService.delete(1);
        verify(userRepository).deleteById(1);
    }

    @Test
    public void editWithNameOnly() {
        UserEditDto edit = new UserEditDto();
        edit.setId(1);
        edit.setName("NewName");
        edit.setEmail(null);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        UserDto res = userService.edit(1, edit);
        assertEquals("NewName", res.getName());
        assertEquals("john@mail.com", res.getEmail());
    }

    @Test
    public void editNotFound() {
        UserEditDto edit = new UserEditDto();
        edit.setId(999);
        when(userRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.edit(999, edit));
    }
}
