package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemRequestService itemRequestService;

    @Test
    void getUsersRequests() throws Exception {
        when(itemRequestService.getUsersRequests(2)).thenReturn(List.of());
        mockMvc.perform(get("/requests").header("X-Sharer-User-Id", "2")).andExpect(status().isOk());
    }

    @Test
    void addRequest() throws Exception {
        ItemRequestCreateDto create = new ItemRequestCreateDto();
        create.setDescription("Need");

        when(itemRequestService.addRequest(eq(2), any())).thenReturn(new ItemRequestResponseDto());

        mockMvc.perform(post("/requests").header("X-Sharer-User-Id", "2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(create)))
                .andExpect(status().isCreated());
    }

    @Test
    void getAllOtherRequests() throws Exception {
        when(itemRequestService.getOthersRequests(2)).thenReturn(List.of());
        mockMvc.perform(get("/requests/all").header("X-Sharer-User-Id", "2")).andExpect(status().isOk());
    }

    @Test
    void getRequest() throws Exception {
        when(itemRequestService.getRequest(5)).thenReturn(new ItemRequestResponseDto());
        mockMvc.perform(get("/requests/5")).andExpect(status().isOk());
    }
}
