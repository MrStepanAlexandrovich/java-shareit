package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionClassesTest {

    @Test
    void exceptionsContainMessage() {
        NotFoundException nfe = new NotFoundException("not found");
        ConflictException ce = new ConflictException("conf");
        ForbiddenException fe = new ForbiddenException("forb");
        BadRequestException bre = new BadRequestException("bad");

        assertEquals("not found", nfe.getMessage());
        assertEquals("conf", ce.getMessage());
        assertEquals("forb", fe.getMessage());
        assertEquals("bad", bre.getMessage());
    }
}

