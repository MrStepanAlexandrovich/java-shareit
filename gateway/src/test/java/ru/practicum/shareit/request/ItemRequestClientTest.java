package ru.practicum.shareit.request;

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

class ItemRequestClientTest {
    private RestTemplate rest;
    private RestTemplateBuilder builder;
    private ItemRequestClient client;
    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        rest = mock(RestTemplate.class);
        builder = mock(RestTemplateBuilder.class);
        when(builder.uriTemplateHandler(any())).thenReturn(builder);
        when(builder.requestFactory(any(java.util.function.Supplier.class))).thenReturn(builder);
        when(builder.build()).thenReturn(rest);

        client = new ItemRequestClient("http://localhost", builder);
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() throws Exception {
        if (mocks != null) mocks.close();
    }

    @Test
    void shouldCallRequests() {
        when(rest.exchange(anyString(), any(HttpMethod.class), any(), eq(Object.class), any(Object[].class))).thenReturn(ResponseEntity.ok("o"));

        assertEquals("o", client.getUsersRequests(1).getBody());
        assertEquals("o", client.addRequest(1, null).getBody());
        assertEquals("o", client.getOthersRequests(1).getBody());
        assertEquals("o", client.getRequest(5).getBody());
    }
}
