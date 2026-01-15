package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;

    @Test
    void createBooking() throws Exception {
        BookingCreateDto create = new BookingCreateDto();
        create.setItemId(5);
        create.setStart(LocalDateTime.now().plusDays(1));
        create.setEnd(LocalDateTime.now().plusDays(2));

        when(bookingService.createBooking(any())).thenReturn(new BookingDto());

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "2")
                        .content(objectMapper.writeValueAsString(create)))
                .andExpect(status().isOk());
    }

    @Test
    void approveBooking() throws Exception {
        when(bookingService.approveBooking(eq(2), eq(1), eq(true))).thenReturn(new BookingDto());

        mockMvc.perform(patch("/bookings/1").param("approved", "true").header("X-Sharer-User-Id", "2"))
                .andExpect(status().isOk());
    }

    @Test
    void getBooking() throws Exception {
        when(bookingService.getBooking(1)).thenReturn(new BookingDto());
        mockMvc.perform(get("/bookings/1")).andExpect(status().isOk());
    }

    @Test
    void getBookingsOfUser() throws Exception {
        when(bookingService.getBookingsOfUser(eq(2), any())).thenReturn(List.of());
        mockMvc.perform(get("/bookings").header("X-Sharer-User-Id", "2")).andExpect(status().isOk());
    }
}

