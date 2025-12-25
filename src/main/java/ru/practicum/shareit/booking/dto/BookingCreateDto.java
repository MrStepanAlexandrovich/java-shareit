package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
public class BookingCreateDto {
    private Integer userId;
    private Integer itemId;
    private LocalDateTime start;
    private LocalDateTime end;

    public Booking toBooking() {
        Booking booking = new Booking();
        booking.setStart(start);
        booking.setEnd(end);
        booking.setItem(new Item());
        booking.getItem().setId(itemId);
        booking.setBooker(new User());
        booking.getBooker().setId(userId);

        return booking;
    }
}
