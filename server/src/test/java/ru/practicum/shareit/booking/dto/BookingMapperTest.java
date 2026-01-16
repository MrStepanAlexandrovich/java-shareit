package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class BookingMapperTest {

    @Test
    public void toBookingAndToBookingDto() {
        BookingCreateDto create = new BookingCreateDto();
        create.setItemId(7);
        create.setUserId(8);
        create.setStart(LocalDateTime.of(2021,1,1,10,0));
        create.setEnd(LocalDateTime.of(2021,1,2,10,0));

        Booking booking = BookingMapper.toBooking(create);
        assertNotNull(booking.getItem());
        assertEquals(7, booking.getItem().getId());
        assertNotNull(booking.getBooker());
        assertEquals(8, booking.getBooker().getId());

        booking.setId(9);
        booking.setStart(create.getStart());
        booking.setEnd(create.getEnd());
        booking.setStatus(Status.APPROVED);
        Item item = new Item();
        item.setId(7);
        item.setName("Thing");
        booking.setItem(item);
        User user = new User(8, "U", "u@e.com");
        booking.setBooker(user);

        BookingDto dto = BookingMapper.toBookingDto(booking);
        assertEquals(9, dto.getId());
        assertEquals(item.getName(), dto.getItem().getName());
        assertEquals(user.getName(), dto.getBooker().getName());
        assertEquals(Status.APPROVED, dto.getStatus());
    }
}

