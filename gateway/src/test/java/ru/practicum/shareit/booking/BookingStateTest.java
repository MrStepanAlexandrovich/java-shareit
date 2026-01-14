package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingState;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BookingStateTest {
    @Test
    void fromShouldReturnCorrectEnumForVariousCases() {
        assertEquals(Optional.of(BookingState.ALL), BookingState.from("ALL"));
        assertEquals(Optional.of(BookingState.CURRENT), BookingState.from("current"));
        assertEquals(Optional.of(BookingState.FUTURE), BookingState.from("FuTuRe"));
        assertEquals(Optional.of(BookingState.PAST), BookingState.from("past"));
        assertEquals(Optional.of(BookingState.REJECTED), BookingState.from("rejected"));
        assertEquals(Optional.of(BookingState.WAITING), BookingState.from("waiting"));
        assertEquals(Optional.empty(), BookingState.from("unknown"));
    }
}

