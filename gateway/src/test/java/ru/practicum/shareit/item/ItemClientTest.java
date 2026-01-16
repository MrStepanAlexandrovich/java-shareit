package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ItemClientTest {
    private RestTemplate rest;
    private RestTemplateBuilder builder;
    private ItemClient client;
    private AutoCloseable mocks;

    @BeforeEach
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        rest = mock(RestTemplate.class);
        builder = mock(RestTemplateBuilder.class);
        when(builder.uriTemplateHandler(any())).thenReturn(builder);
        when(builder.requestFactory(any(java.util.function.Supplier.class))).thenReturn(builder);
        when(builder.build()).thenReturn(rest);

        client = new ItemClient("http://localhost", builder);
    }

    @org.junit.jupiter.api.AfterEach
    public void tearDown() throws Exception {
        if (mocks != null) mocks.close();
    }

    @Test
    public void shouldCallEndpoints() {
        when(rest.exchange(anyString(), any(HttpMethod.class), any(), eq(Object.class), any(Object[].class)))
                .thenReturn(ResponseEntity.ok("x"));

        assertEquals("x", client.add(1L, null).getBody());
        assertEquals("x", client.edit(1L, 2L, null).getBody());
        assertEquals("x", client.getItem(2L, 1L).getBody());
        assertEquals("x", client.getItems(1L).getBody());
        assertEquals("x", client.search(1L, "text").getBody());
        assertEquals("x", client.addComment(2L, 1L, null).getBody());

        verify(rest, atLeastOnce()).exchange(anyString(), any(HttpMethod.class), any(), eq(Object.class), any(Object[].class));
    }
}
