package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {
    public static Booking toBooking(BookingCreateDto bookingCreateDto) {
        Booking booking = new Booking();
        booking.setStart(bookingCreateDto.getStart());
        booking.setEnd(bookingCreateDto.getEnd());
        booking.setItem(new Item());
        booking.getItem().setId(bookingCreateDto.getItemId());
        booking.setBooker(new User());
        booking.getBooker().setId(bookingCreateDto.getUserId());

        return booking;
    }
}
