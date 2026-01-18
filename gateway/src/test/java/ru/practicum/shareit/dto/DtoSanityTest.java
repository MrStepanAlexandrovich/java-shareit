package ru.practicum.shareit.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserEditDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DtoSanityTest {
    @Test
    public void itemDtoGettersSetters() {
        ItemDto i = new ItemDto();
        i.setId(1);
        i.setName("n");
        i.setDescription("d");
        i.setAvailable(true);
        i.setOwner(2);
        i.setRequestId(3);
        i.setComments(List.of(new CommentDto(1, "a", LocalDateTime.now(), "t")));

        assertEquals(1, i.getId());
        assertEquals("n", i.getName());
        assertTrue(i.getAvailable());
    }

    @Test
    public void userDtoGettersSetters() {
        UserCreateDto u = new UserCreateDto();
        u.setName("x");
        u.setEmail("e@e.com");
        assertEquals("x", u.getName());
        assertEquals("e@e.com", u.getEmail());

        UserEditDto ue = new UserEditDto();
        ue.setId(5);
        ue.setName("n");
        ue.setEmail("p@p.com");
        assertEquals(5, ue.getId());
        assertEquals("p@p.com", ue.getEmail());
    }

    @Test
    public void requestDto() {
        ItemRequestCreateDto r = new ItemRequestCreateDto();
        r.setDescription("d");
        assertEquals("d", r.getDescription());
    }
}

