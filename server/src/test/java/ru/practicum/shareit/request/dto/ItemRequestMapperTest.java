package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ItemRequestMapperTest {

    @Test
    void toItemRequestAndResponse() {
        ItemRequestCreateDto create = new ItemRequestCreateDto();
        create.setDescription("Need drill");

        ItemRequest req = ItemRequestMapper.toItemRequest(create);
        assertEquals("Need drill", req.getDescription());
        assertNotNull(req.getCreated());

        ItemRequest ir = new ItemRequest();
        ir.setId(11);
        ir.setDescription("Something");
        ir.setCreated(LocalDateTime.of(2022,2,2,2,2));
        User requester = new User(4, "R", "r@e.com");
        ir.setRequester(requester);

        ItemRequestResponseDto dto = ItemRequestMapper.toItemRequestResponseDto(ir);
        assertEquals(11, dto.getId());
        assertEquals("R", dto.getRequester().getName());
        assertEquals(ir.getCreated(), dto.getCreated());
    }
}

