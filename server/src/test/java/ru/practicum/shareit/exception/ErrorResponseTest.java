package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

public class ErrorResponseTest {

    @Test
    public void constructorSetsFields() {
        ErrorResponse r = new ErrorResponse(HttpStatus.BAD_REQUEST, "err", "msg");
        assertEquals(HttpStatus.BAD_REQUEST, r.getStatus());
        assertEquals("err", r.getError());
        assertEquals("msg", r.getMessage());
        assertNotNull(r.getTimestamp());
    }
}

