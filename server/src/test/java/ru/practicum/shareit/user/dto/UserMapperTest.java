package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserEditDto;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.*;

public class UserMapperTest {

    @Test
    public void toUserDtoAndBack() {
        User user = new User(1, "John", "john@example.com");
        UserDto dto = UserMapper.toUserDto(user);

        assertEquals(1, dto.getId());
        assertEquals("John", dto.getName());
        assertEquals("john@example.com", dto.getEmail());

        UserEditDto edit = new UserEditDto();
        edit.setId(1);
        edit.setName("John");
        edit.setEmail("john@example.com");
        User fromEdit = UserMapper.toUser(edit);
        assertEquals(edit.getId(), fromEdit.getId());
        assertEquals(edit.getName(), fromEdit.getName());
        assertEquals(edit.getEmail(), fromEdit.getEmail());

        UserCreateDto create = new UserCreateDto(2, "Jane", "jane@example.com");
        User fromCreate = UserMapper.toUser(create);
        assertEquals(create.getId(), fromCreate.getId());
        assertEquals(create.getName(), fromCreate.getName());
        assertEquals(create.getEmail(), fromCreate.getEmail());
    }
}

