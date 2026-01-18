package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

public class ErrorHandlerTest {

    @Test
    public void handleAll() {
        ErrorHandler handler = new ErrorHandler();

        ErrorResponse r1 = handler.handleConflictException(new ConflictException("conf"));
        assertEquals(HttpStatus.CONFLICT, r1.getStatus());
        assertEquals("error", r1.getError());
        assertEquals("conf", r1.getMessage());

        ErrorResponse r2 = handler.handleNotFoundException(new NotFoundException("nf"));
        assertEquals(HttpStatus.NOT_FOUND, r2.getStatus());

        ErrorResponse r3 = handler.handleForbiddenException(new ForbiddenException("forb"));
        assertEquals(HttpStatus.FORBIDDEN, r3.getStatus());

        ErrorResponse r4 = handler.handleBadRequestException(new BadRequestException("bad"));
        assertEquals(HttpStatus.BAD_REQUEST, r4.getStatus());
    }
}

