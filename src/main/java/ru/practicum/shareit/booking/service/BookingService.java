package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface BookingService {
    Booking createBooking(Booking booking);

    Booking response(Booking booking);

    Booking getBooking(int bookingId);

    Collection<Item> getUsersItemsThatBooked(
            int userId,
            Booking.Status status);

    Collection<Booking> getBookingsOfUser(int userId, Booking.Status status);
}
