package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ItemMapperTest {

    @Test
    void toItemDtoAndBack() {
        User owner = new User(10, "Owner", "owner@example.com");
        Item item = new Item();
        item.setId(5);
        item.setName("Drill");
        item.setDescription("Electric drill");
        item.setIsAvailable(true);
        item.setOwner(owner);

        ItemDto dto = ItemMapper.toItemDto(item);
        assertEquals(5, dto.getId());
        assertEquals("Drill", dto.getName());
        assertEquals("Electric drill", dto.getDescription());
        assertTrue(dto.getAvailable());
        assertEquals(10, dto.getOwner());

        Item converted = ItemMapper.toItem(dto);
        assertEquals(dto.getId(), converted.getId());
        assertEquals(dto.getName(), converted.getName());
        assertEquals(dto.getDescription(), converted.getDescription());
        assertEquals(dto.getAvailable(), converted.getIsAvailable());

        Comment comment = new Comment();
        comment.setId(1);
        comment.setText("Nice");
        comment.setCreated(LocalDateTime.now());
        comment.setAuthor(owner);

        item.setComments(List.of(comment));
        ItemWithBookingsDto withBookings = ItemMapper.toItemWithBookingsDto(item);
        assertEquals(5, withBookings.getId());
        assertEquals("Drill", withBookings.getName());
        assertEquals(10, withBookings.getOwner());
        assertNotNull(withBookings.getComments());
    }
}

