package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void addItem() throws Exception {
        ItemCreateDto create = new ItemCreateDto();
        create.setName("N");
        create.setDescription("D");
        create.setAvailable(true);

        ItemDto returned = new ItemDto();
        returned.setId(1);
        returned.setName("N");
        returned.setDescription("D");
        returned.setAvailable(true);
        returned.setOwner(10);

        when(itemService.add(any(), eq(10))).thenReturn(returned);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "10")
                        .content(objectMapper.writeValueAsString(create)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getItem() throws Exception {
        when(itemService.get(5)).thenReturn(new ItemWithBookingsDto());
        mockMvc.perform(get("/items/5").header("X-Sharer-User-Id", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void search() throws Exception {
        when(itemService.search("q")).thenReturn(List.of(new ItemDto()));
        mockMvc.perform(get("/items/search").param("text", "q").header("X-Sharer-User-Id", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }
}
