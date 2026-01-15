package ru.practicum.shareit.booking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class BookingClientTest {
    private RestTemplate rest;
    private RestTemplateBuilder builder;
    private BookingClient client;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        rest = mock(RestTemplate.class, invocation -> {
            if (ResponseEntity.class.isAssignableFrom(invocation.getMethod().getReturnType())) {
                return ResponseEntity.ok("resp");
            }
            return RETURNS_DEFAULTS.answer(invocation);
        });
        builder = mock(RestTemplateBuilder.class);
        when(builder.uriTemplateHandler(any())).thenReturn(builder);
        when(builder.requestFactory(any(java.util.function.Supplier.class))).thenReturn(builder);
        when(builder.build()).thenReturn(rest);

        when(rest.postForEntity(anyString(), any(), any(Class.class)))
                .thenReturn(ResponseEntity.ok("resp"));

        when(rest.exchange(anyString(), any(), any(), any(Class.class), any(Object[].class)))
                .thenReturn(ResponseEntity.ok("resp"));

        when(rest.exchange(anyString(), any(), any(), any(Class.class), any(Map.class)))
                .thenReturn(ResponseEntity.ok("resp"));

        when(rest.exchange(anyString(), any(), any(), any(ParameterizedTypeReference.class), any(Object[].class)))
                .thenReturn(ResponseEntity.ok("resp"));

        when(rest.exchange(anyString(), any(), any(), any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok("resp"));

        when(rest.exchange(any(java.net.URI.class), any(), any(), any(Class.class)))
                .thenReturn(ResponseEntity.ok("resp"));

        client = new BookingClient("http://localhost", builder);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (mocks != null) {
            mocks.close();
        }
    }

    @Test
    void shouldGetBookingsCallRestExchange() {
        when(rest.exchange(anyString(), eq(HttpMethod.GET), any(), eq(Object.class), any(Map.class)))
                .thenReturn(ResponseEntity.ok("ok"));

        var res = client.getBookings(1L, ru.practicum.shareit.booking.dto.BookingState.ALL, 0, 10);
        assertEquals("ok", res.getBody());

        verify(rest).exchange(eq("?state={state}&from={from}&size={size}"), eq(HttpMethod.GET), any(), eq(Object.class), any(Map.class));
    }

    @Test
    void shouldBookAndGetApproveAndOwner() {
        when(rest.exchange(anyString(), any(), any(), eq(Object.class), any(Object[].class)))
                .thenReturn(ResponseEntity.ok("resp"));

        when(rest.exchange(anyString(), eq(HttpMethod.POST), any(), eq(Object.class)))
                .thenReturn(ResponseEntity.ok("resp"));

        when(rest.exchange(anyString(), any(), any(), any(ParameterizedTypeReference.class), any(Object[].class)))
                .thenReturn(ResponseEntity.ok("resp"));

        when(rest.exchange(anyString(), any(), any(), any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok("resp"));

        when(rest.exchange(anyString(), any(), any(), eq(Object.class), any(Map.class)))
                .thenReturn(ResponseEntity.ok("resp"));

        assertEquals("resp", client.bookItem(1L, null).getBody());
        assertEquals("resp", client.getBooking(1L, 5L).getBody());
        assertEquals("resp", client.approveBooking(1L, 5L, true).getBody());
        assertEquals("resp", client.getUsersItemsThatBooked(1L, ru.practicum.shareit.booking.dto.BookingState.ALL).getBody());

        verify(rest).exchange(eq(""), eq(HttpMethod.POST), any(), eq(Object.class));
        verify(rest).exchange(eq("/5"), eq(HttpMethod.GET), any(), eq(Object.class), any(Object[].class));
    }
}
