package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class UserClientTest {
    private RestTemplate rest;
    private UserClient client;
    private AutoCloseable mocks;

    @BeforeEach
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        rest = mock(RestTemplate.class);
        org.springframework.boot.web.client.RestTemplateBuilder builder = mock(org.springframework.boot.web.client.RestTemplateBuilder.class);
        when(builder.uriTemplateHandler(any())).thenReturn(builder);
        when(builder.requestFactory(any(java.util.function.Supplier.class))).thenReturn(builder);
        when(builder.build()).thenReturn(rest);

        client = new UserClient("http://localhost", builder);
    }

    @org.junit.jupiter.api.AfterEach
    public void tearDown() throws Exception {
        if (mocks != null) mocks.close();
    }

    @Test
    public void shouldCallUserEndpoints() {
        when(rest.exchange(anyString(), any(HttpMethod.class), any(), eq(Object.class), any(Object[].class))).thenReturn(ResponseEntity.ok("u"));

        assertEquals("u", client.getUser(1).getBody());
        assertEquals("u", client.createUser(null).getBody());
        assertEquals("u", client.editUser(1, null).getBody());
        client.removeUser(2);

        verify(rest, atLeastOnce()).exchange(anyString(), any(HttpMethod.class), any(), eq(Object.class), any(Object[].class));
    }
}
